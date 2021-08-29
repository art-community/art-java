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

package io.art.http.constants;

import lombok.*;
import reactor.netty.http.*;

public interface HttpModuleConstants {
    interface Errors {
        String SPECIFICATION_NOT_FOUND = "Specification was not found for service method identifiers: {0}";
        String CONFIGURATION_PARAMETER_NOT_EXISTS = "HTTP configuration parameter does not exists: ''{0}''";
    }

    interface Warnings {
        String ROUTE_PATH_NOT_EXISTS = "Specified file route path does not exists: {0}";
    }

    interface Messages {
        String HTTP_COMMUNICATOR_LOGGER = "http-communicator";
        String HTTP_SERVER_LOGGER = "http-server";
        String HTTP_DISPOSING = "Disposing HTTP";
        String HTTP_SERVER_STARTED = "HTTP {0} server started - {1}:{2}";
        String HTTP_SERVER_STOPPED = "HTTP {0} server stopped - {1}:{2}";
        String HTTP_COMMUNICATOR_STARTED = "HTTP communicator connector started: {0}";
        String HTTP_COMMUNICATOR_STOPPED = "HTTP communicator connector stopped: {0}";
    }

    interface ConfigurationKeys {
        String HTTP_SECTION = "http";
        String ROUTES_SECTION = "routes";

        String URL_KEY = "url";
        String PATH_KEY = "path";
        String METHOD_KEY = "method";
        String WS_AGGREGATE_FRAMES_KEY = "ws.aggregateFrames";
        String FILE_PATH_KEY = "file.path";
        String FORWARD_KEY = "forward";
        String IDLE_TIMEOUT_KEY = "idleTimeout";
        String PROTOCOL_KEY = "protocol";
        String ACCESS_LOG_KEY = "accessLog";
        String WIRETAP_LOG_KEY = "wiretapLog";
        String RETRY_KEY = "retry";
        String KEEP_ALIVE_KEY = "keepAlive";
        String FOLLOW_REDIRECT_KEY = "followRedirect";
        String RESPONSE_TIMEOUT_KEY = "responseTimeout";
    }


    @Getter
    @AllArgsConstructor
    enum HttpRouteType {
        GET("get"),
        POST("post"),
        PUT("put"),
        DELETE("delete"),
        OPTIONS("options"),
        HEAD("head"),
        PATCH("patch"),
        PATH("path"),
        WS("WS");

        private final String type;

        public static HttpRouteType httpRouteType(String type, HttpRouteType fallback) {
            for (HttpRouteType value : HttpRouteType.values()) {
                if (value.name().equalsIgnoreCase(type)) return value;
            }
            return fallback;
        }
    }

    interface Defaults {
        int DEFAULT_PORT = 80;
    }

    static HttpProtocol httpProtocol(String protocol, HttpProtocol fallback) {
        for (HttpProtocol value : HttpProtocol.values()) {
            if (value.name().equalsIgnoreCase(protocol)) return value;
        }
        return fallback;
    }
}
