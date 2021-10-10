package io.art.http.communicator;

import io.art.core.annotation.*;
import io.art.http.constants.HttpModuleConstants.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.http.constants.HttpModuleConstants.HttpRouteType.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Public
@Getter(value = PACKAGE)
public class HttpCommunicationDecorator {
    private HttpRouteType route;
    private UnaryOperator<HttpClient> client;
    private UnaryOperator<HttpHeaders> headers;
    private UnaryOperator<String> uri = UnaryOperator.identity();
    private final Set<String> pathParameters = set();
    private final Map<String, String> queryParameters = map();
    private final Map<String, Cookie> cookies = map();

    public HttpCommunicationDecorator get() {
        this.route = GET;
        return this;
    }

    public HttpCommunicationDecorator post() {
        this.route = POST;
        return this;
    }

    public HttpCommunicationDecorator put() {
        this.route = PUT;
        return this;
    }

    public HttpCommunicationDecorator patch() {
        this.route = PATCH;
        return this;
    }

    public HttpCommunicationDecorator options() {
        this.route = OPTIONS;
        return this;
    }

    public HttpCommunicationDecorator head() {
        this.route = HEAD;
        return this;
    }

    public HttpCommunicationDecorator ws() {
        this.route = WS;
        return this;
    }

    public HttpCommunicationDecorator pathParameter(String value) {
        pathParameters.add(value);
        return this;
    }

    public HttpCommunicationDecorator queryParameter(String name, String value) {
        queryParameters.put(name, value);
        return this;
    }

    public HttpCommunicationDecorator headers(UnaryOperator<HttpHeaders> headers) {
        this.headers = headers;
        return this;
    }

    public HttpCommunicationDecorator client(UnaryOperator<HttpClient> decorator) {
        this.client = decorator;
        return this;
    }

    public HttpCommunicationDecorator uri(String uri) {
        this.uri = ignore -> uri;
        return this;
    }

    public HttpCommunicationDecorator uri(UnaryOperator<String> mapper) {
        this.uri = mapper;
        return this;
    }
}
