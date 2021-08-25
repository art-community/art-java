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
import java.time.*;

public interface HttpModuleConstants {
    interface Errors {
        String SPECIFICATION_NOT_FOUND = "Specification was not found for service method identifiers: {0}";
        String CONFIGURATION_PARAMETER_NOT_EXISTS = "HTTP configuration parameter does not exists: ''{0}''";
    }

    interface LoggingMessages {
        String HTTP_DISPOSING = "Disposing HTTP";
        String SERVER_STARTED = "HTTP server started";
        String SERVER_STOPPED = "HTTP server stopped";
        String COMMUNICATOR_STARTED = "HTTP communicator started\nConnector: {0}\nSetup payload\n{1}";
        String COMMUNICATOR_STOPPED = "HTTP communicator stopped\nConnector: {0}\nSetup payload\n{1}";
    }

    interface ConfigurationKeys {
        String HTTP_SECTION = "http";
        String RECONNECT_SECTION = "reconnect";
        String KEEP_ALIVE_SECTION = "keepAlive";

        String TRANSPORT_WS_BASE_URL_KEY = "transport.ws.baseUrl";

        String LOGGING_KEY = "logging";
        String FRAGMENTATION_MTU_KEY = "fragmentationMtu";

        String POLICY_KEY = "policy";
        String BACKOFF_MAX_ATTEMPTS_KEY = "backoff.maxAttempts";
        String BACKOFF_MIN_BACKOFF_KEY = "backoff.minBackoff";
        String FIXED_DELAY_MAX_ATTEMPTS_KEY = "fixedDelay.maxAttempts";
        String FIXED_DELAY_KEY = "fixedDelay.delay";
        String MAX_KEY = "max";
        String MAX_IN_ROW_KEY = "maxInRow";
        String MAX_INBOUND_PAYLOAD_SIZE_KEY = "maxInboundPayloadSize";

        String ROUTES_SECTION = "routes";

        String INTERVAL_KEY = "interval";
        String MAX_LIFE_TIME_KEY = "maxLifeTime";

        String PATH_KEY = "path";
        String METHOD_KEY = "method";

        String WS_AGGREGATE_FRAMES_KEY = "ws.aggregateFrames";
        String ROUTED_PATH_KEY = "routed.path";
    }

    enum HttpRouteType {
        GET,
        POST,
        PUT,
        DELETE,
        OPTIONS,
        HEAD,
        PATCH,
        DIRECTORY,
        FILE,
        WEBSOCKET
    }

    interface Defaults {
        long DEFAULT_RETRY_MAX_ATTEMPTS = 3;
        Duration DEFAULT_RETRY_MIN_BACKOFF = Duration.ofSeconds(1);
        Duration DEFAULT_RETRY_FIXED_DELAY = Duration.ofSeconds(1);
        int DEFAULT_RETRY_MAX = 1;
        int DEFAULT_RETRY_MAX_IN_ROW = 1;
        int DEFAULT_PORT = 80;
        Duration DEFAULT_KEEP_ALIVE_INTERVAL = Duration.ofSeconds(20);
        Duration DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME = Duration.ofSeconds(90);
    }

    @Getter
    @AllArgsConstructor
    enum RetryPolicy {
        BACKOFF("backoff"),
        FIXED_DELAY("fixedDelay"),
        MAX("max"),
        MAX_IN_A_ROW("maxInARow"),
        INDEFINITELY("indefinitely");

        private final String policy;

        public static RetryPolicy httpRetryPolicy(String policy, RetryPolicy fallback) {
            if (BACKOFF.policy.equalsIgnoreCase(policy)) return BACKOFF;
            if (FIXED_DELAY.policy.equalsIgnoreCase(policy)) return FIXED_DELAY;
            if (MAX.policy.equalsIgnoreCase(policy)) return MAX;
            if (MAX_IN_A_ROW.policy.equalsIgnoreCase(policy)) return MAX_IN_A_ROW;
            if (INDEFINITELY.policy.equalsIgnoreCase(policy)) return INDEFINITELY;
            return fallback;
        }
    }
}
