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

package io.art.rsocket.constants;

import io.art.communicator.constants.CommunicatorConstants.*;
import io.rsocket.*;
import io.rsocket.util.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.constants.StringConstants.*;
import static java.time.Duration.*;
import java.time.*;

public interface RsocketModuleConstants {
    Payload EMPTY_PAYLOAD = EmptyPayload.INSTANCE;
    Mono<Payload> EMPTY_PAYLOAD_MONO = Mono.just(EmptyPayload.INSTANCE);

    interface Errors {
        String SERVICE_METHOD_NOT_FOUND = "Service method was not found for service method identifiers: {0}";
    }

    interface LoggingMessages {
        String RSOCKET_COMMUNICATOR_LOGGER = "rsocket-communicator";
        String RSOCKET_SERVER_LOGGER = "rsocket-server";
        String SERVER_STARTED = "RSocket server started: {0}";
        String SERVER_STOPPED = "RSocket server stopped";
        String COMMUNICATOR_STARTED = "RSocket communicator started\nConnector: {0}\nSetup payload: {1}\n";
        String COMMUNICATOR_STOPPED = "RSocket communicator stopped\nConnector: {0}";

        String FIRE_AND_FORGET_REQUEST_LOG = "RSocket executing fireAndForget()\nData:\n{0}\nMetadata:\n{1}";
        String FIRE_AND_FORGET_RESPONSE_LOG = "RSocket fireAndForget() completed";
        String FIRE_AND_FORGET_EXCEPTION_LOG = "RSocket fireAndForget() failed\nException:\n{0}";

        String METADATA_PUSH_REQUEST_LOG = "RSocket executing metadataPush().Data:\n{0}\nMetadata:\n{1}";
        String METADATA_PUSH_RESPONSE_LOG = "RSocket metadataPush() completed";
        String METADATA_PUSH_EXCEPTION_LOG = "RSocket metadataPush() failed\nException:\n{0}";

        String REQUEST_RESPONSE_REQUEST_LOG = "RSocket executing requestResponse()\nData:\n{0}\nMetadata:\n{1}";
        String RESPONSE_RESPONSE_LOG = "RSocket requestResponse() completed\nData:\n{0}\nMetadata:\n{1}";
        String REQUEST_RESPONSE_EXCEPTION_LOG = "RSocket requestResponse() failed\nException:\n{0}";

        String REQUEST_STREAM_REQUEST_LOG = "RSocket executing requestStream()\nData:\n{0}\nMetadata:\n{1}";
        String REQUEST_STREAM_RESPONSE_LOG = "RSocket requestStream() processed\nData:\n{0}\nMetadata:\n{1}";
        String REQUEST_STREAM_EXCEPTION_LOG = "RSocket requestStream() failed\nException:\n{0}";

        String REQUEST_CHANNEL_REQUEST_LOG = "RSocket requestChannel() processed\nData:\n{0}\nMetadata:\n{1}";
        String REQUEST_CHANNEL_RESPONSE_LOG = "RSocket requestChannel() processed\nData:\n{0}\nMetadata:\n{1}";
        String REQUEST_CHANNEL_EXCEPTION_LOG = "RSocket requestChannel() failed\nException:\n{0}";
    }

    interface ConfigurationKeys {
        String RSOCKET_SECTION = "rsocket";
        String SERVER_SECTION = "server";
        String COMMUNICATOR_SECTION = "communicator";
        String RESUME_SECTION = "produce";
        String RETRY_SECTION = "retry";
        String RECONNECT_SECTION = "reconnect";
        String KEEP_ALIVE_SECTION = "keepAlive";

        String DATA_FORMAT_KEY = "dataFormat";
        String META_DATA_FORMAT_KEY = "metaDataFormat";
        String SERVICE_ID_KEY = "serviceId";
        String METHOD_ID_KEY = "methodId";

        String BALANCER_KEY = "balancer";
        String GROUP_KEY = "group";
        String SINGLE_KEY = "single";
        String TRANSPORT_PORT_KEY = "transport.port";
        String TRANSPORT_HOST_KEY = "transport.host";
        String TRANSPORT_TIMEOUT_CONNECTION_KEY = "transport.timeout";
        String TRANSPORT_WS_PATH_KEY = "transport.ws.path";
        String TRANSPORT_TCP_MAX_FRAME_LENGTH = "transport.tcp.maxFrameLength";
        String TRANSPORT_SSL_SECTION = "transport.ssl";

        String PAYLOAD_DECODER_KEY = "payloadDecoder";
        String LOGGING_KEY = "logging";
        String FRAGMENTATION_MTU_KEY = "fragmentationMtu";

        String CLEANUP_STORE_ON_KEEP_ALIVE_KEY = "cleanupStoreOnKeepAlive";
        String SESSION_DURATION_KEY = "sessionDuration";
        String STREAM_TIMEOUT_KEY = "streamTimeout";
        String POLICY_KEY = "policy";
        String BACKOFF_MAX_ATTEMPTS_KEY = "backoff.maxAttempts";
        String BACKOFF_MIN_BACKOFF_KEY = "backoff.minBackoff";
        String FIXED_DELAY_MAX_ATTEMPTS_KEY = "fixedDelay.maxAttempts";
        String FIXED_DELAY_KEY = "fixedDelay.delay";
        String MAX_KEY = "max";
        String MAX_IN_ROW_KEY = "maxInRow";
        String MAX_INBOUND_PAYLOAD_SIZE_KEY = "maxInboundPayloadSize";

        String INTERVAL_KEY = "interval";
        String MAX_LIFE_TIME_KEY = "maxLifeTime";

        String CONNECTORS_KEY = "connectors";

        String CERTIFICATE_KEY = "certificate";
        String KEY_KEY = "key";
        String PASSWORD_KEY = "key";
    }

    interface ContextKeys {
        String REQUESTER_RSOCKET_KEY = "requesterRsocket";
        String SETUP_PAYLOAD_KEY = "setupPayload";
    }

    interface Defaults {
        long DEFAULT_RETRY_MAX_ATTEMPTS = 3;
        Duration DEFAULT_RETRY_MIN_BACKOFF = ofSeconds(1);
        Duration DEFAULT_RETRY_FIXED_DELAY = ofSeconds(1);
        int DEFAULT_RETRY_MAX = 1;
        int DEFAULT_RETRY_MAX_IN_ROW = 1;
        int DEFAULT_PORT = 9000;
        Duration DEFAULT_TIMEOUT = ofSeconds(10);
        Duration DEFAULT_RESUME_SESSION_DURATION = ofHours(1);
        Duration DEFAULT_RESUME_STREAM_TIMEOUT = ofHours(1);
        Duration DEFAULT_KEEP_ALIVE_INTERVAL = ofSeconds(20);
        Duration DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME = ofSeconds(90);
        String DEFAULT_HTTP_PATH = SLASH;
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

        public static RetryPolicy rsocketRetryPolicy(String policy, RetryPolicy fallback) {
            if (BACKOFF.policy.equalsIgnoreCase(policy)) return BACKOFF;
            if (FIXED_DELAY.policy.equalsIgnoreCase(policy)) return FIXED_DELAY;
            if (MAX.policy.equalsIgnoreCase(policy)) return MAX;
            if (MAX_IN_A_ROW.policy.equalsIgnoreCase(policy)) return MAX_IN_A_ROW;
            if (INDEFINITELY.policy.equalsIgnoreCase(policy)) return INDEFINITELY;
            return fallback;
        }
    }

    @Getter
    @AllArgsConstructor
    enum PayloadDecoderMode {
        ZERO_COPY("zeroCopy"),
        DEFAULT("default");

        private final String decoder;

        public static PayloadDecoderMode rsocketPayloadDecoder(String decoder) {
            return rsocketPayloadDecoder(decoder, DEFAULT);
        }

        public static PayloadDecoderMode rsocketPayloadDecoder(String decoder, PayloadDecoderMode fallback) {
            if (ZERO_COPY.decoder.equalsIgnoreCase(decoder)) return ZERO_COPY;
            if (DEFAULT.decoder.equalsIgnoreCase(decoder)) return DEFAULT;
            return fallback;
        }
    }

    @Getter
    @AllArgsConstructor
    enum BalancerMethod {
        ROUND_ROBIN("roundRobin"),
        WEIGHTED("weighted");

        private final String method;

        public static BalancerMethod rsocketBalancer(String method, BalancerMethod fallback) {
            if (ROUND_ROBIN.method.equalsIgnoreCase(method)) return ROUND_ROBIN;
            if (WEIGHTED.method.equalsIgnoreCase(method)) return WEIGHTED;
            return fallback;
        }
    }

    enum CommunicationMode {
        FIRE_AND_FORGET,
        REQUEST_RESPONSE,
        REQUEST_STREAM,
        REQUEST_CHANNEL,
        METADATA_PUSH;
    }
}
