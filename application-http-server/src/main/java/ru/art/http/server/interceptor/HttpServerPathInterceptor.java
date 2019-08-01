package ru.art.http.server.interceptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpServerPathInterceptor {
    private final HttpServerInterceptor interceptor;
    private final String path;
}
