package io.art.http.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.mime.*;
import io.art.core.property.*;
import io.art.http.meta.MetaHttp.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaPortalPackage.MetaHttpDefaultPortalClass.*;
import io.art.meta.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.http.communicator.HttpCommunicationFactory.*;
import static io.art.http.configuration.HttpConnectorConfiguration.*;
import static io.art.http.constants.HttpModuleConstants.Defaults.*;
import static io.art.http.portal.HttpDefaultPortal.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import java.time.*;
import java.util.function.*;

@Public
public class HttpDefaultCommunicator {
    private final HttpCommunicationDecorator decorator = new HttpCommunicationDecorator();
    private final HttpConnectorConfigurationBuilder connector = httpConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private final static LazyProperty<MetaHttpExecutionCommunicatorClass> communicatorClass = lazy(() -> Meta.declaration(HttpExecutionCommunicator.class));
    private volatile CommunicatorProxy<HttpExecutionCommunicator> proxy;

    public HttpDefaultCommunicator retry(boolean value) {
        connector.retry(value);
        return this;
    }

    public HttpDefaultCommunicator keepAlive(boolean value) {
        connector.keepAlive(value);
        return this;
    }

    public HttpDefaultCommunicator verbose(boolean value) {
        connector.verbose(value);
        return this;
    }

    public HttpDefaultCommunicator compress(boolean value) {
        connector.compress(value);
        return this;
    }

    public HttpDefaultCommunicator wiretapLog(boolean value) {
        connector.wiretapLog(value);
        return this;
    }

    public HttpDefaultCommunicator followRedirect(boolean value) {
        connector.followRedirect(value);
        return this;
    }

    public HttpDefaultCommunicator url(String url) {
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
        connector.responseTimeout(value);
        return this;
    }

    public HttpDefaultCommunicator wsAggregateFrames(int value) {
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

    public HttpDefaultResponse execute() {
        return new HttpDefaultResponse(this, createCommunicator().execute(Flux.empty()));
    }

    public HttpDefaultResponse execute(HttpDefaultRequest body) {
        return new HttpDefaultResponse(this, createCommunicator().execute(Flux.just(body.getInput())));
    }

    public HttpDefaultResponse execute(HttpReactiveRequest body) {
        return new HttpDefaultResponse(this, createCommunicator().execute(body.getInput()));
    }

    public void dispose() {
        apply(proxy, notNull -> notNull.getActions().values().forEach(CommunicatorAction::dispose));
    }

    private HttpExecutionCommunicator createCommunicator() {
        return (proxy = communicatorProxy(communicatorClass.get(), () -> createDefaultHttpCommunication(connector.build())))
                .getCommunicator()
                .decorate(ignore -> decorator)
                .input(BYTES)
                .output(BYTES);
    }
}
