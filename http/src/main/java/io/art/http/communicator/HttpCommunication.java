/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.http.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.exception.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.http.constants.HttpModuleConstants.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import io.art.meta.model.*;
import io.art.transport.payload.*;
import io.netty.buffer.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.http.client.HttpClient.*;
import reactor.netty.http.websocket.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.Property.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

public class HttpCommunication implements Communication {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = Logging.logger(HttpCommunication.class);
    private final HttpConnectorConfiguration connectorConfiguration;
    private final Property<HttpClient> client;
    private final Property<Function<Flux<Object>, Flux<Object>>> communication;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;
    private CommunicatorAction action;

    private final static ThreadLocal<HttpCommunicationDecorator> decorator = new ThreadLocal<>();

    public HttpCommunication(Supplier<HttpClient> client, HttpModuleConfiguration module, HttpConnectorConfiguration connector) {
        this.connectorConfiguration = connector;
        this.client = property(client).listenConsumer(() -> module.getConsumer()
                .connectorConsumers()
                .consumerFor(connector.getConnector()));
        communication = property(this::communication).listenProperties(this.client);
    }

    @Override
    public void initialize(CommunicatorAction action) {
        inputMappingType = action.getInputType();
        if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
            inputMappingType = inputMappingType.parameters().get(0);
        }

        outputMappingType = action.getOutputType();
        if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
            outputMappingType = outputMappingType.parameters().get(0);
        }
        client.initialize();
        this.action = action;
    }

    @Override
    public void dispose() {
        client.dispose();
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return communication.get().apply(input);
    }

    @Override
    public String toString() {
        return connectorConfiguration.getConnector();
    }

    private Function<Flux<Object>, Flux<Object>> communication() {
        TransportPayloadReader reader = transportPayloadReader(connectorConfiguration.getDataFormat());
        TransportPayloadWriter writer = transportPayloadWriter(connectorConfiguration.getDataFormat());
        return input -> communicate(reader, writer, input);
    }

    private Flux<Object> communicate(TransportPayloadReader reader, TransportPayloadWriter writer, Flux<Object> input) {
        HttpCommunicationDecorator decorator;
        ProcessingConfiguration.ProcessingConfigurationBuilder builder = ProcessingConfiguration.builder()
                .reader(reader)
                .writer(writer)
                .input(input);
        return isNull(decorator = HttpCommunication.decorator.get()) ? simpleCommunication(builder) : decoratedCommunication(builder, decorator);
    }

    private Flux<Object> decoratedCommunication(ProcessingConfiguration.ProcessingConfigurationBuilder builder, HttpCommunicationDecorator decorator) {
        HttpCommunication.decorator.remove();

        builder.route(orElse(decorator.getRoute(), extractRouteType(action.getId().getActionId(), GET)));
        HttpClient client = orElse(decorator.getClient(), UnaryOperator.<HttpClient>identity()).apply(this.client.get());

        HttpHeaders headers = new DefaultHttpHeaders()
                .set(CONTENT_TYPE, toMimeType(connectorConfiguration.getDataFormat()))
                .set(ACCEPT, toMimeType(connectorConfiguration.getDataFormat()));

        connectorConfiguration.getHeaders().forEach(headers::add);
        client = client.headers(current -> current.add(orElse(decorator.getHeaders(), UnaryOperator.<HttpHeaders>identity()).apply(headers)));

        connectorConfiguration.getCookies().values().forEach(client::cookie);
        Map<String, Cookie> cookies = decorator.getCookies();
        for (Cookie cookie : cookies.values()) {
            client = client.cookie(cookie);
        }

        StringBuilder uri = new StringBuilder(connectorConfiguration.getUri().make(action.getId()));
        for (String parameter : decorator.getPathParameters()) {
            uri.append(SLASH).append(parameter);
        }
        if (!decorator.getQueryParameters().isEmpty()) {
            String parameterString = decorator
                    .getQueryParameters()
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + EQUAL + entry.getValue())
                    .collect(joining(AMPERSAND));
            uri.append(QEUSTION).append(parameterString);
        }

        return processCommunication(builder.client(client).uri(uri).build());
    }

    private Flux<Object> simpleCommunication(ProcessingConfiguration.ProcessingConfigurationBuilder builder) {
        HttpRouteType route = extractRouteType(action.getId().getActionId(), GET);
        HttpClient client = this.client.get();

        HttpHeaders headers = new DefaultHttpHeaders()
                .set(CONTENT_TYPE, toMimeType(connectorConfiguration.getDataFormat()))
                .set(ACCEPT, toMimeType(connectorConfiguration.getDataFormat()));

        connectorConfiguration.getHeaders().forEach(headers::add);
        client = client.headers(current -> current.add(headers));

        for (Cookie cookie : connectorConfiguration.getCookies().values()) {
            client = client.cookie(cookie);
        }

        StringBuilder uri = new StringBuilder(connectorConfiguration.getUri().make(action.getId()));
        return processCommunication(builder.route(route).client(client).uri(uri).build());
    }

    private Flux<Object> processCommunication(ProcessingConfiguration configuration) {
        switch (configuration.route) {
            case GET:
                ResponseReceiver<?> responseReceiver = configuration.client.get().uri(configuration.uri.toString());
                return readResponse(configuration.reader, responseReceiver);
            case OPTIONS:
                responseReceiver = configuration.client.options().uri(configuration.uri.toString());
                return readResponse(configuration.reader, responseReceiver);
            case HEAD:
                responseReceiver = configuration.client.head().uri(configuration.uri.toString());
                return readResponse(configuration.reader, responseReceiver);
            case POST:
                RequestSender requestSender = configuration.client.post().uri(configuration.uri.toString());
                responseReceiver = writeRequest(configuration.input, configuration.writer, requestSender);
                return readResponse(configuration.reader, responseReceiver);
            case PUT:
                requestSender = configuration.client.put().uri(configuration.uri.toString());
                responseReceiver = writeRequest(configuration.input, configuration.writer, requestSender);
                return readResponse(configuration.reader, responseReceiver);
            case DELETE:
                requestSender = configuration.client.delete().uri(configuration.uri.toString());
                responseReceiver = writeRequest(configuration.input, configuration.writer, requestSender);
                return readResponse(configuration.reader, responseReceiver);
            case PATCH:
                requestSender = configuration.client.patch().uri(configuration.uri.toString());
                responseReceiver = writeRequest(configuration.input, configuration.writer, requestSender);
                return readResponse(configuration.reader, responseReceiver);
            case WS:
                return configuration
                        .client
                        .websocket()
                        .uri(configuration.uri.toString())
                        .handle((inbound, outbound) -> handleWebSocket(configuration, inbound, outbound));
        }

        throw new ImpossibleSituationException();
    }

    private Flux<Object> readResponse(TransportPayloadReader reader, ResponseReceiver<?> responseReceiver) {
        if (isNull(outputMappingType)) {
            return responseReceiver.response().thenMany(Flux.empty());
        }
        return responseReceiver.responseSingle((response, data) -> data.map(bytes -> reader.read(bytes, outputMappingType)))
                .flux()
                .filter(payload -> !payload.isEmpty())
                .map(TransportPayload::getValue);
    }

    private ResponseReceiver<?> writeRequest(Flux<Object> input, TransportPayloadWriter writer, HttpClient.RequestSender requestSender) {
        if (isNull(inputMappingType)) return requestSender.send(Flux.empty());
        return requestSender.send(input.map(data -> writer.write(typed(inputMappingType, data))));
    }

    private Flux<Object> handleWebSocket(ProcessingConfiguration configuration, WebsocketInbound inbound, WebsocketOutbound outbound) {
        if (isNull(inputMappingType)) {
            sendWebSocket(outbound, Flux.empty());
            if (isNull(outputMappingType)) return Flux.empty();
            return receiveWebSocket(configuration, inbound);
        }

        sendWebSocket(outbound, configuration.input.map(data -> configuration.writer.write(typed(inputMappingType, data))));
        if (isNull(outputMappingType)) return Flux.empty();
        return receiveWebSocket(configuration, inbound);
    }

    private Flux<Object> receiveWebSocket(ProcessingConfiguration configuration, WebsocketInbound inbound) {
        return inbound
                .aggregateFrames()
                .receive()
                .map(bytes -> configuration.reader.read(bytes, outputMappingType))
                .filter(payload -> !payload.isEmpty())
                .map(TransportPayload::getValue);
    }

    private void sendWebSocket(WebsocketOutbound outbound, Flux<ByteBuf> input) {
        outbound.send(input.doOnComplete(outbound::sendClose)).then().subscribe();
    }

    @Builder
    private static class ProcessingConfiguration {
        final TransportPayloadReader reader;
        final TransportPayloadWriter writer;
        final Flux<Object> input;
        final HttpRouteType route;
        final HttpClient client;
        final StringBuilder uri;
    }

    static void decorateHttpCommunication(UnaryOperator<HttpCommunicationDecorator> decorator) {
        HttpCommunication.decorator.set(decorator.apply(new HttpCommunicationDecorator()));
    }
}

