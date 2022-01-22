/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import io.art.core.exception.*;
import lombok.*;
import reactor.netty.http.*;

public interface HttpModuleConstants {
    interface Warnings {
        String ROUTE_PATH_NOT_EXISTS = "Specified file route path does not exists: {0}";
    }

    interface Messages {
        String HTTP_LOGGER = "http";
        String HTTP_COMMUNICATOR_LOGGER = "http-communicator";
        String HTTP_SERVER_LOGGER = "http-server";
        String HTTP_SERVER_STOPPED = "HTTP server stopped - {0}:{1}";
        String HTTP_COMMUNICATOR_STARTED = "HTTP communicator connector started: {0}";
        String HTTP_COMMUNICATOR_STOPPED = "HTTP communicator connector stopped: {0}";

        String HTTP_LAUNCHED_MESSAGE_PART = "HTTP module launched\n\t";
        String HTTP_SERVER_MESSAGE_PART = "Server - {0}://{1}:{2}\n\t";
        String HTTP_SERVICES_MESSAGE_PART = "Services:\n\t\t{0}\n\t";
        String HTTP_SERVICE_MESSAGE_PART = "[service = {0}] {1}";
        String HTTP_SERVICE_METHOD_MESSAGE_PART = "[method = {0}] {1}";
        String HTTP_CONNECTORS_MESSAGE_PART = "Connectors:\n\t\t{0}\n\t";
        String HTTP_COMMUNICATORS_MESSAGE_PART = "Communicators:\n\t\t{0}";
        String HTTP_COMMUNICATOR_MESSAGE_PART = "[communicator = {0}] {1}";
        String HTTP_COMMUNICATOR_ACTION_MESSAGE_PART = "[connector = {0}, action = {1}] {2}";
        String HTTP_CONNECTOR_URL_PART = "url = {0}, ";
        String HTTP_CONNECTOR_URI_PART = "uri = {0}";
        String HTTP_CONNECTOR_URI_TRANSFORMED = "custom";
        String HTTP_CONNECTOR_URI_BY_ACTION = "based on communicator";
    }

    interface Errors {
        String WRITING_FILE_TO_DIRECTORY = "Paths {0} is a directory. We can't write a response to a directory. Change the path to a file location";
    }

    interface ConfigurationKeys {
        String HTTP_SECTION = "http";
        String ROUTES_SECTION = "routes";

        String URL_KEY = "url";
        String URI_KEY = "uri";
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
        String PATH_PARAMETERS_KEY = "pathParameters";
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
        WS("ws");

        private final String type;

        public static HttpRouteType httpRouteType(String type, HttpRouteType fallback) {
            for (HttpRouteType value : HttpRouteType.values()) {
                if (value.name().equalsIgnoreCase(type)) return value;
            }
            return fallback;
        }

        public static boolean methodHasRouteTypePrefix(String methodName) {
            for (HttpRouteType value : values()) {
                if (value != PATH && methodName.toLowerCase().startsWith(value.type)) return true;
            }
            return false;
        }

        public static HttpRouteType extractRouteType(String methodName) {
            for (HttpRouteType value : values()) {
                if (value != PATH && methodName.toLowerCase().startsWith(value.type)) return value;
            }
            throw new ImpossibleSituationException();
        }

        public static HttpRouteType extractRouteType(String methodName, HttpRouteType fallback) {
            for (HttpRouteType value : values()) {
                if (value != PATH && methodName.toLowerCase().startsWith(value.type)) return value;
            }
            return fallback;
        }
    }

    interface Defaults {
        int DEFAULT_PORT = 8080;
        int DEFAULT_AGGREGATE_FRAMES = 65536;
        String DEFAULT_CONNECTOR_ID = "default";
    }

    static HttpProtocol httpProtocol(String protocol, HttpProtocol fallback) {
        for (HttpProtocol value : HttpProtocol.values()) {
            if (value.name().equalsIgnoreCase(protocol)) return value;
        }
        return fallback;
    }
}
