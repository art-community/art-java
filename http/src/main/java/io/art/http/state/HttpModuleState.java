/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.state;

import io.art.core.module.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.*;
import reactor.netty.http.server.*;

import java.net.*;
import java.util.*;
import java.util.function.*;

public class HttpModuleState implements ModuleState {
    private final ThreadLocal<HttpThreadLocalContext> threadLocalContext = new ThreadLocal<>();

    public void localContext(Function<HttpThreadLocalContext, HttpThreadLocalContext> functor) {
        threadLocalContext.set(functor.apply(threadLocalContext.get()));
    }

    public void localContext(HttpThreadLocalContext context) {
        threadLocalContext.set(context);
    }

    public HttpThreadLocalContext localContext() {
        return threadLocalContext.get();
    }


    @Getter
    public static class HttpThreadLocalContext {
        private final HttpServerRequest request;
        @Getter(value = AccessLevel.PRIVATE)
        private final HttpServerResponse response;
        private final Map<String, String> parameters;
        private final HttpHeaders headers;
        private final Map<CharSequence, Set<Cookie>> cookies;
        private final String scheme;
        private final InetSocketAddress hostAddress;
        private final InetSocketAddress remoteAddress;
        public Integer status = 201;

        public HttpThreadLocalContext(HttpServerRequest request, HttpServerResponse response) {
            this.request = request;
            this.response = response;
            parameters = request.params();
            headers = request.requestHeaders();
            cookies = request.cookies();
            scheme = request.scheme();
            hostAddress = request.hostAddress();
            remoteAddress = request.remoteAddress();
        }

        public void setStatus(int status){
//            response.status(status);
            this.status = status;
        }

    }
}
