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

import io.art.meta.model.*;
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
        String HTTP_SERVER_STOPPED = "HTTP server has been stopped - {0}:{1}";
        String HTTP_COMMUNICATOR_STARTED = "HTTP communicator connector has been started: {0}";
        String HTTP_COMMUNICATOR_STOPPED = "HTTP communicator connector has been stopped: {0}";

        String HTTP_LAUNCHED_MESSAGE_PART = "HTTP module has been launched\n\t";
        String HTTP_SERVER_MESSAGE_PART = "Server - {0}://{1}:{2}\n\t";
        String HTTP_ROUTES_MESSAGE_PART = "Routes:\n\t\t{0}\n\t";
        String HTTP_ROUTE_METHOD_MESSAGE_PART = "{0} {1}\n\t\t\t[service = {2}, method = {3}] {4} '{'{5}'}'";
        String HTTP_CONNECTORS_PART = "Connectors:\n\t\t{0}\n\t";
        String HTTP_COMMUNICATORS_MESSAGE_PART = "Communications:\n\t\t{0}";
        String HTTP_COMMUNICATOR_ACTION_MESSAGE_PART = "{0} {1}\n\t\t\t[connector = {2}, communicator = {3}, action = {4}] {5} '{'{6}'}'";
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

        public static HttpRouteType extractRouteType(MetaMethod<MetaClass<?>, ?> method) {
            for (HttpRouteType value : values()) {
                if (value != PATH && method.name().toLowerCase().startsWith(value.type)) return value;
            }
            return method.parameters().isEmpty() ? GET : POST;
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
