package io.art.http.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.mime.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.property.Property.*;
import static io.art.http.communicator.HttpCommunicationFactory.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import static io.art.http.meta.MetaHttp.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaCommunicatorPackage.*;
import static io.art.http.module.HttpModule.*;
import static io.art.meta.Meta.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import java.time.*;
import java.util.function.*;

@Public
public class HttpDefaultCommunicator {
    private final static LazyProperty<MetaHttpBuiltinCommunicatorClass> communicatorClass = lazy(() -> declaration(HttpBuiltinCommunicator.class));
    private final HttpCommunicationDecorator decorator = new HttpCommunicationDecorator().input(BYTES).output(BYTES);
    private final Property<CommunicatorProxy<HttpBuiltinCommunicator>> proxy = property(this::createCommunicator);
    private HttpConnectorConfigurationBuilder connector = httpConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();

    public HttpDefaultCommunicator from(ConnectorIdentifier connectorId) {
        return from(httpModule().configuration().getConnectors().get(connectorId.id()));
    }

    public HttpDefaultCommunicator from(String connectorId) {
        return from(httpModule().configuration().getConnectors().get(connectorId));
    }

    public HttpDefaultCommunicator from(HttpConnectorConfiguration from) {
        refreshCommunicator();
        connector = from.toBuilder();
        return this;
    }

    public HttpDefaultCommunicator retry(boolean value) {
        refreshCommunicator();
        connector.retry(value);
        return this;
    }

    public HttpDefaultCommunicator keepAlive(boolean value) {
        refreshCommunicator();
        connector.keepAlive(value);
        return this;
    }

    public HttpDefaultCommunicator verbose(boolean value) {
        refreshCommunicator();
        connector.verbose(value);
        return this;
    }

    public HttpDefaultCommunicator compress(boolean value) {
        refreshCommunicator();
        connector.compress(value);
        return this;
    }

    public HttpDefaultCommunicator wiretapLog(boolean value) {
        refreshCommunicator();
        connector.wiretapLog(value);
        return this;
    }

    public HttpDefaultCommunicator followRedirect(boolean value) {
        refreshCommunicator();
        connector.followRedirect(value);
        return this;
    }

    public HttpDefaultCommunicator url(String url) {
        refreshCommunicator();
        String urlWithoutSchema = url.substring(url.indexOf(SCHEME_DELIMITER) + SCHEME_DELIMITER.length());
        int lastSlashIndex = urlWithoutSchema.lastIndexOf(SLASH);

        if (lastSlashIndex == -1) {
            connector.url(EMPTY_STRING);
            decorator.uri(url);
            return this;
        }

        if (lastSlashIndex != urlWithoutSchema.length() - 1) {
            String uri = urlWithoutSchema.substring(lastSlashIndex);
            decorator.uri(uri);
            connector.url(urlWithoutSchema.substring(0, lastSlashIndex));
            return this;
        }

        connector.url(url);
        return this;
    }

    public HttpDefaultCommunicator responseTimeout(Duration value) {
        refreshCommunicator();
        connector.responseTimeout(value);
        return this;
    }

    public HttpDefaultCommunicator wsAggregateFrames(int value) {
        refreshCommunicator();
        connector.wsAggregateFrames(value);
        return this;
    }

    public HttpDefaultCommunicator get(String url) {
        url(url);
        decorator.get();
        return this;
    }

    public HttpDefaultCommunicator post(String url) {
        url(url);
        decorator.post();
        return this;
    }

    public HttpDefaultCommunicator put(String url) {
        url(url);
        decorator.put();
        return this;
    }

    public HttpDefaultCommunicator patch(String url) {
        url(url);
        decorator.patch();
        return this;
    }

    public HttpDefaultCommunicator options(String url) {
        url(url);
        decorator.options();
        return this;
    }

    public HttpDefaultCommunicator head(String url) {
        url(url);
        decorator.head();
        return this;
    }

    public HttpDefaultCommunicator ws(String url) {
        url(url);
        decorator.ws();
        return this;
    }

    public HttpDefaultCommunicator pathParameter(String value) {
        decorator.pathParameter(value);
        return this;
    }

    public HttpDefaultCommunicator queryParameter(String name, String value) {
        decorator.queryParameter(name, value);
        return this;
    }

    public HttpDefaultCommunicator headers(UnaryOperator<HttpHeaders> headers) {
        decorator.headers(headers);
        return this;
    }

    public HttpDefaultCommunicator client(UnaryOperator<HttpClient> client) {
        decorator.client(client);
        return this;
    }

    public HttpDefaultCommunicator cookie(String name, Cookie cookie) {
        decorator.cookie(name, cookie);
        return this;
    }

    public HttpDefaultCommunicator accept(MimeType type) {
        decorator.headers(headers -> headers.add(ACCEPT, type.toString()));
        return this;
    }

    public HttpDefaultCommunicator content(MimeType type) {
        decorator.headers(headers -> headers.add(CONTENT_TYPE, type.toString()));
        return this;
    }

    public HttpBlockingResponse execute() {
        Flux<byte[]> output = proxy.get().getCommunicator().decorate(ignore -> decorator).execute(Flux.empty());
        return new HttpBlockingResponse(this, output);
    }

    public HttpBlockingResponse execute(HttpBlockingRequest body) {
        Flux<byte[]> output = proxy.get().getCommunicator().decorate(ignore -> decorator).execute(Flux.just(body.getInput()));
        return new HttpBlockingResponse(this, output);
    }

    public HttpBlockingResponse execute(HttpReactiveRequest body) {
        Flux<byte[]> output = proxy.get().getCommunicator().decorate(ignore -> decorator).execute(body.getInput());
        return new HttpBlockingResponse(this, output);
    }

    public void dispose() {
        if (proxy.initialized()) {
            proxy.get().getActions().values().forEach(CommunicatorAction::dispose);
        }
    }

    private CommunicatorProxy<HttpBuiltinCommunicator> createCommunicator() {
        return communicatorProxy(communicatorClass.get(), () -> createDefaultHttpCommunication(connector.build()));
    }

    private void refreshCommunicator() {
        if (proxy.initialized()) {
            dispose();
            proxy.dispose();
        }
    }
}
