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
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import io.art.meta.model.*;
import io.art.transport.payload.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import static io.art.core.property.Property.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class HttpCommunication implements Communication {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = Logging.logger(HttpCommunication.class);
    private final HttpConnectorConfiguration connectorConfiguration;
    private final Property<HttpClient> client;
    private final Property<Function<Flux<Object>, Flux<Object>>> communication;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;

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
    }

    @Override
    public void dispose() {
        client.dispose();
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return communication.get().apply(input);
    }

    private Function<Flux<Object>, Flux<Object>> communication() {
        TransportPayloadReader reader = transportPayloadReader(connectorConfiguration.getDataFormat());
        TransportPayloadWriter writer = transportPayloadWriter(connectorConfiguration.getDataFormat());
        HttpClient client = this.client.get();
        return input -> client
                .get()
                .responseSingle((response, data) -> data.map(bytes -> reader.read(bytes, outputMappingType)))
                .flux()
                .filter(payload -> !payload.isEmpty())
                .map(TransportPayload::getValue);
    }

    @Override
    public String toString() {
        return connectorConfiguration.getConnector();
    }
}

