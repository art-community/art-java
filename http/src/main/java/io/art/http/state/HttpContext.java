package io.art.http.state;

import io.art.value.immutable.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import reactor.netty.http.server.*;
import reactor.util.context.*;

import java.net.*;
import java.util.*;

import static io.art.core.collector.MapCollector.mapCollector;
import static io.art.core.factory.MapFactory.map;
import static io.art.value.factory.PrimitivesFactory.stringPrimitive;
import static java.util.Objects.isNull;

@Getter
public class HttpContext {
    private final HttpServerRequest request;
    @Getter(value = AccessLevel.PRIVATE)
    private final HttpServerResponse response;
    private final Map<String, Primitive> pathParams;
    private final Map<String, Primitive> queryParams;
    private final HttpHeaders headers;
    private final Map<CharSequence, Set<Cookie>> cookies;
    private final String scheme;
    private final InetSocketAddress hostAddress;
    private final InetSocketAddress remoteAddress;

    private HttpContext(HttpServerRequest request, HttpServerResponse response) {
        this.request = request;
        this.response = response;
        pathParams = isNull(request.params()) ? map() :
                request.params().entrySet().stream()
                        .collect(mapCollector(Map.Entry::getKey, entry -> stringPrimitive(entry.getValue())));
        queryParams = parseQuery(request);
        headers = request.requestHeaders();
        cookies = request.cookies();
        scheme = request.scheme();
        hostAddress = request.hostAddress();
        remoteAddress = request.remoteAddress();
    }

    public static HttpContext from(HttpServerRequest request, HttpServerResponse response){
        return new HttpContext(request, response);
    }

    public static HttpContext from(ContextView context){
        return context.get(HttpContext.class);
    }

    public HttpResponseStatus responseStatus(){
        return response.status();
    }

    public HttpHeaders responseHeaders(){
        return response.responseHeaders();
    }

    public Map<CharSequence, Set<Cookie>> responseCookies() {
        return response.cookies();
    }

    public HttpContext status(int status){
        response.status(status);
        return this;
    }

    public HttpContext status(HttpResponseStatus status){
        response.status(status);
        return this;
    }

    public HttpContext cookie(Cookie cookie){
        response.addCookie(cookie);
        return this;
    }

    public HttpContext header(CharSequence name, CharSequence value){
        response.header(name, value);
        return this;
    }

    public HttpContext headers(HttpHeaders headers){
        response.headers(headers);
        return this;
    }

    public HttpContext compression(boolean compress){
        response.compression(compress);
        return this;
    }

    private static Map<String, Primitive> parseQuery(HttpServerRequest request){
        String[] parts = request.uri().split(request.path());
        return parts.length != 2 ? map() :
                Arrays.stream(parts[1].substring(1).split("&"))
                        .sequential()
                        .map(query -> query.split("="))
                        .collect(mapCollector(item -> item[0], item -> stringPrimitive(item[1])));
    }

}
