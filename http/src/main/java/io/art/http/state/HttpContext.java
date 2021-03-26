package io.art.http.state;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import reactor.netty.http.server.*;

import java.net.*;
import java.util.Map;
import java.util.Set;

@Getter
public class HttpContext {
    private final HttpServerRequest request;
    @Getter(value = AccessLevel.PRIVATE)
    private final HttpServerResponse response;
    private final Map<String, String> parameters;
    private final HttpHeaders headers;
    private final Map<CharSequence, Set<Cookie>> cookies;
    private final String scheme;
    private final InetSocketAddress hostAddress;
    private final InetSocketAddress remoteAddress;

    private HttpContext(HttpServerRequest request, HttpServerResponse response) {
        this.request = request;
        this.response = response;
        parameters = request.params();
        headers = request.requestHeaders();
        cookies = request.cookies();
        scheme = request.scheme();
        hostAddress = request.hostAddress();
        remoteAddress = request.remoteAddress();
    }

    public static HttpContext from(HttpServerRequest request, HttpServerResponse response){
        return new HttpContext(request, response);
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

}
