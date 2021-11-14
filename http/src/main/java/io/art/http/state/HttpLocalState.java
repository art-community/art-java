package io.art.http.state;

import io.art.core.annotation.*;
import io.art.http.configuration.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import lombok.experimental.*;
import reactor.netty.http.server.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.net.*;
import java.util.*;

@Getter
@Public
@Accessors(fluent = true)
public class HttpLocalState {
    private final HttpServerRequest request;
    private final HttpServerResponse response;
    private final Map<String, String> pathParameters;
    private final Map<String, String> queryParameters;
    private final HttpHeaders requestHeaders;
    private final Map<CharSequence, Set<Cookie>> requestCookies;
    private final String scheme;
    private final InetSocketAddress hostAddress;
    private final InetSocketAddress remoteAddress;
    private final HttpRouteConfiguration routeConfiguration;

    private HttpLocalState(HttpServerRequest request, HttpServerResponse response, HttpRouteConfiguration routeConfiguration) {
        this.request = request;
        this.response = response;
        this.routeConfiguration = routeConfiguration;
        pathParameters = isNull(request.params()) ? emptyMap() : request.params();
        queryParameters = parseQuery(request);
        requestHeaders = request.requestHeaders();
        requestCookies = request.cookies();
        scheme = request.scheme();
        hostAddress = request.hostAddress();
        remoteAddress = request.remoteAddress();
    }

    public static HttpLocalState httpLocalState(HttpServerRequest request, HttpServerResponse response, HttpRouteConfiguration routeConfiguration) {
        return new HttpLocalState(request, response, routeConfiguration);
    }

    public HttpResponseStatus responseStatus() {
        return response.status();

    }

    public HttpHeaders responseHeaders() {
        return response.responseHeaders();
    }

    public Map<CharSequence, Set<Cookie>> responseCookies() {
        return response.cookies();
    }

    public HttpLocalState responseStatus(int status) {
        response.status(status);
        return this;
    }

    public HttpLocalState responseStatus(HttpResponseStatus status) {
        response.status(status);
        return this;
    }

    public HttpLocalState responseCookie(Cookie cookie) {
        response.addCookie(cookie);
        return this;
    }

    public HttpLocalState responseHeader(String name, String value) {
        response.header(name, value);
        return this;
    }

    public HttpLocalState responseHeaders(HttpHeaders headers) {
        response.headers(headers);
        return this;
    }

    public HttpLocalState responseCompression(boolean compress) {
        response.compression(compress);
        return this;
    }

    private static Map<String, String> parseQuery(HttpServerRequest request) {
        String[] parts = request.uri().split(request.path());
        return parts.length != 2 ? emptyMap() : stream(parts[1].substring(1).split(AMPERSAND))
                .sequential()
                .map(query -> query.split(EQUAL))
                .collect(mapCollector(item -> item[0], item -> item[1]));
    }
}
