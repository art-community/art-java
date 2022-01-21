package io.art.http.communicator;

import io.art.core.property.*;
import io.art.http.meta.MetaHttp.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaPortalPackage.MetaHttpDefaultPortalClass.*;
import io.art.meta.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.communicator.HttpCommunicationFactory.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import static io.art.http.portal.HttpDefaultPortal.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.time.*;
import java.util.function.*;

public class HttpDefaultCommunicator {
    private final HttpCommunicationDecorator decorator = new HttpCommunicationDecorator();
    private final HttpConnectorConfigurationBuilder connector = httpConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private final static LazyProperty<MetaHttpExecutionCommunicatorClass> communicatorClass = lazy(() -> Meta.declaration(HttpExecutionCommunicator.class));

    private HttpDefaultCommunicator retry(boolean value) {
        connector.retry(value);
        return this;
    }

    private HttpDefaultCommunicator keepAlive(boolean value) {
        connector.keepAlive(value);
        return this;
    }

    private HttpDefaultCommunicator verbose(boolean value) {
        connector.verbose(value);
        return this;
    }

    private HttpDefaultCommunicator compress(boolean value) {
        connector.compress(value);
        return this;
    }

    private HttpDefaultCommunicator wiretapLog(boolean value) {
        connector.wiretapLog(value);
        return this;
    }

    private HttpDefaultCommunicator followRedirect(boolean value) {
        connector.followRedirect(value);
        return this;
    }

    private HttpDefaultCommunicator url(String url) {
        if (!url.endsWith(SLASH)) {
            decorator.uri(url.substring(url.lastIndexOf(SLASH)));
        }
        connector.url(url);
        return this;
    }

    private HttpDefaultCommunicator responseTimeout(Duration value) {
        connector.responseTimeout(value);
        return this;
    }

    private HttpDefaultCommunicator wsAggregateFrames(int value) {
        connector.wsAggregateFrames(value);
        return this;
    }

    public HttpDefaultCommunicator get() {
        decorator.get();
        return this;
    }

    public HttpDefaultCommunicator post() {
        decorator.post();
        return this;
    }

    public HttpDefaultCommunicator put() {
        decorator.put();
        return this;
    }

    public HttpDefaultCommunicator patch() {
        decorator.patch();
        return this;
    }

    public HttpDefaultCommunicator options() {
        decorator.options();
        return this;
    }

    public HttpDefaultCommunicator head() {
        decorator.head();
        return this;
    }

    public HttpDefaultCommunicator ws() {
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

    public HttpDefaultResponse execute() {
        return new HttpDefaultResponse(createCommunicator().execute(Flux.empty()));
    }

    public HttpDefaultResponse execute(HttpDefaultRequest body) {
        return new HttpDefaultResponse(createCommunicator().execute(body.getInput()));
    }

    private HttpExecutionCommunicator createCommunicator() {
        return communicatorProxy(communicatorClass.get(), () -> createDefaultHttpCommunication(connector.build()))
                .getCommunicator()
                .decorate(ignore -> decorator)
                .input(BYTES)
                .output(BYTES);
    }
}
