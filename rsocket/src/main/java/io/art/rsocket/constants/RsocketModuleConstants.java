/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.constants;

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
        String CLIENTS_EMPTY = "RSocket communicator hasn't registered clients";
        String WRITING_FILE_TO_DIRECTORY = "Paths {0} is directory. We can't write response to directory. Change path to file location";
    }

    interface Messages {
        String RSOCKET_LOGGER = "rsocket";
        String RSOCKET_COMMUNICATOR_LOGGER = "rsocket-communicator";
        String RSOCKET_SERVER_LOGGER = "rsocket-server";
        String RSOCKET_SERVER_STOPPED = "RSocket {0} server has been stopped - {1}:{2}";

        String RSOCKET_COMMUNICATOR_STARTED = "RSocket communicator connector has been started: {0}\nSetup payload:\n{1}";
        String RSOCKET_COMMUNICATOR_STOPPED = "RSocket communicator connector has been stopped: {0}";

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

        String RSOCKET_LAUNCHED_MESSAGE_PART = "RSocket module has been launched\n\t";
        String RSOCKET_TCP_SERVER_MESSAGE_PART = "TCP Server - {0}://{1}:{2}\n\t";
        String RSOCKET_WS_SERVER_MESSAGE_PART = "WS Server - {0}://{1}:{2}\n\t";
        String RSOCKET_SERVICES_MESSAGE_PART = "Services:\n\t\t{0}\n\t";
        String RSOCKET_SERVICE_MESSAGE_PART = "[service = {0}] {1}";
        String RSOCKET_SERVICE_METHOD_MESSAGE_PART = "[method = {0}] '{'{1}'}'";
        String RSOCKET_TCP_CONNECTORS_MESSAGE_PART = "TCP Connectors:\n\t\t{0}\n\t";
        String RSOCKET_WS_CONNECTORS_MESSAGE_PART = "WS Connectors:\n\t\t{0}\n\t";
        String RSOCKET_COMMUNICATORS_MESSAGE_PART = "Communicators:\n\t\t{0}";
        String RSOCKET_COMMUNICATOR_MESSAGE_PART = "[communicator = {0}] {1}";
        String RSOCKET_COMMUNICATOR_ACTION_MESSAGE_PART = "[connector = {0}, action = {1}] '{'{2}'}'";
    }

    interface ConfigurationKeys {
        String RSOCKET_SECTION = "rsocket";
        String RESUME_SECTION = "resume";
        String RETRY_SECTION = "retry";
        String RECONNECT_SECTION = "reconnect";
        String KEEP_ALIVE_SECTION = "keepAlive";
        String BALANCER_KEY = "balancer";

        String WS_PATH_KEY = "ws.path";
        String TCP_MAX_FRAME_LENGTH_KEY = "tcp.maxFrameLength";

        String FRAGMENTATION_MTU_KEY = "fragmentationMtu";
        String CLEANUP_STORE_ON_KEEP_ALIVE_KEY = "cleanupStoreOnKeepAlive";
        String SESSION_DURATION_KEY = "sessionDuration";
        String STREAM_TIMEOUT_KEY = "streamTimeout";


        String MAX_INBOUND_PAYLOAD_SIZE_KEY = "maxInboundPayloadSize";

        String INTERVAL_KEY = "interval";
        String MAX_LIFE_TIME_KEY = "maxLifeTime";
    }

    interface Defaults {
        int DEFAULT_PORT = 9000;
        Duration DEFAULT_TIMEOUT = ofSeconds(30);
        Duration DEFAULT_RESUME_SESSION_DURATION = ofHours(1);
        Duration DEFAULT_RESUME_STREAM_TIMEOUT = ofSeconds(10);
        Duration DEFAULT_KEEP_ALIVE_INTERVAL = ofSeconds(20);
        Duration DEFAULT_KEEP_ALIVE_MAX_LIFE_TIME = ofSeconds(90);
        String DEFAULT_WS_PATH = SLASH;
        String DEFAULT_CONNECTOR_ID = "default";
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

    String TCP_SERVER_TYPE = "TCP";
    String WS_SERVER_TYPE = "WS";
}
