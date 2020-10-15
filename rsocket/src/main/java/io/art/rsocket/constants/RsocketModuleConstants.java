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

package io.art.rsocket.constants;

import io.art.rsocket.exception.*;
import lombok.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import java.time.*;

public interface RsocketModuleConstants {
    String RSOCKET_CLIENT_DISPOSING = "Disposing RSocket client";

    interface ExceptionMessages {
        String SPECIFICATION_NOT_FOUND = "Setup payload was null or not contained serviceId, methodId. Default service method id was not specified in configuration";
        String UNSUPPORTED_RETRY_POLICY = "Retry policy not support: ''{0}''";
        String SERVICE_NOT_EXISTS = "Service with id ''{0}'' does not exists in service registry";
        String METHOD_NOT_EXISTS = "Rsocket method with id ''{0}'' for service {1} does not exists in rsocketService";
        String SERVICE_NOT_SUPPORTED_RSOCKET = "Service with id ''{0}'' has not 'RSOCKET' service type";
        String UNSUPPORTED_DATA_FORMAT = "Unsupported payload data format: ''{0}''";
        String UNSUPPORTED_TRANSPORT = "Unsupported RSocket transport: ''{0}''";
        String INVALID_RSOCKET_COMMUNICATION_CONFIGURATION = "Some required fields in RSocket communication configuration are null: ";
        String FAILED_TO_READ_PAYLOAD = "Payload reading failed with exception: ''{0}''";
        String INTERCEPTING_DATA_TYPE_NULL = "Intercepting data type is null";
    }

    interface LoggingMessages {
        String RSOCKET_TCP_SERVER_STARTED_MESSAGE = "RSocket TCP server started";
        String RSOCKET_WS_SERVER_STARTED_MESSAGE = "RSocket WS server started";
        String RSOCKET_TCP_COMMUNICATOR_STARTED_MESSAGE = "RSocket TCP communicator to {0}:{1,number,#} started";
        String RSOCKET_WS_COMMUNICATOR_STARTED_MESSAGE = "RSocket WebSocket communicator to {0}:{1,number,#} started";
        String RSOCKET_WS_COMMUNICATOR_CREATED_MESSAGE = "RSocket WebSocket communicator to {0}:{1,number,#} created";
        String RSOCKET_TCP_COMMUNICATOR_CREATED_MESSAGE = "RSocket TCP communicator to {0}:{1,number,#} created";
        String RSOCKET_STOPPED = "RSocket Server stopped";
        String RSOCKET_FIRE_AND_FORGET_REQUEST_LOG = "RSocket executing fireAndForget() with request data: {0} and metadata: {1}";
        String RSOCKET_FIRE_AND_FORGET_RESPONSE_LOG = "RSocket fireAndForget() completed";
        String RSOCKET_FIRE_AND_FORGET_EXCEPTION_LOG = "RSocket fireAndForget() failed with exception: {0}";
        String RSOCKET_METADATA_PUSH_REQUEST_LOG = "RSocket executing metadataPush() with request data: {0} and metadata: {1}";
        String RSOCKET_METADATA_PUSH_RESPONSE_LOG = "RSocket metadataPush() completed";
        String RSOCKET_METADATA_PUSH_EXCEPTION_LOG = "RSocket metadataPush() failed with exception: {0}";
        String RSOCKET_REQUEST_RESPONSE_REQUEST_LOG = "RSocket executing requestResponse() with request data: {0} and metadata: {1}";
        String RSOCKET_REQUEST_RESPONSE_RESPONSE_LOG = "RSocket requestResponse() completed with response data: {0} and metadata: {1}";
        String RSOCKET_REQUEST_RESPONSE_EXCEPTION_LOG = "RSocket requestResponse() failed with exception: {0}";
        String RSOCKET_REQUEST_STREAM_REQUEST_LOG = "RSocket executing requestStream() with request data: {0} and metadata: {1}";
        String RSOCKET_REQUEST_STREAM_RESPONSE_LOG = "RSocket requestStream() processed with response data: {0} and metadata: {1}";
        String RSOCKET_REQUEST_STREAM_EXCEPTION_LOG = "RSocket requestStream() failed with exception: {0}";
        String RSOCKET_REQUEST_CHANNEL_REQUEST_LOG = "RSocket requestChannel() processed with request data: {0} and metadata: {1}";
        String RSOCKET_REQUEST_CHANNEL_RESPONSE_LOG = "RSocket requestChannel() processed with response data: {0} and metadata: {1}";
        String RSOCKET_REQUEST_CHANNEL_EXCEPTION_LOG = "RSocket requestChannel() failed with exception: {0}";
    }

    interface ConfigurationKeys {
        String RSOCKET_SERVER_DEFAULT_SERVICE_ID_KEY = "rsocket.server.services.default.serviceId";
        String RSOCKET_SERVER_DEFAULT_METHOD_ID_KEY = "rsocket.server.services.default.methodId";
        String RSOCKET_DEFAULT_SERVER_DATA_FORMAT_KEY = "rsocket.server.defaults.dataFormat";
        String RSOCKET_SERVER_TRACING_KEY = "rsocket.server.tracing";
        String RSOCKET_SERVER_FRAGMENTATION_MTU_KEY = "rsocket.server.fragmentationMtu";
        String RSOCKET_SERVER_RESUME_SECTION = "rsocket.server.resume";
        String RSOCKET_SERVER_RESUME_CLEANUP_STORE_ON_KEEP_ALIVE_KEY = "rsocket.server.resume.cleanupStoreOnKeepAlive";
        String RSOCKET_SERVER_RESUME_SESSION_DURATION_KEY = "rsocket.server.resume.sessionDuration";
        String RSOCKET_SERVER_RESUME_STREAM_TIMEOUT_KEY = "rsocket.server.resume.streamTimeout";
        String RSOCKET_SERVER_RESUME_RETRY_POLICY_KEY = "rsocket.server.resume.retry.policy";
        String RSOCKET_SERVER_RESUME_RETRY_BACKOFF_MAX_ATTEMPTS_KEY = "rsocket.server.resume.retry.backoff.maxAttempts";
        String RSOCKET_SERVER_RESUME_RETRY_BACKOFF_MIN_BACKOFF_KEY = "rsocket.server.resume.retry.backoff.minBackoff";
        String RSOCKET_SERVER_RESUME_RETRY_FIXED_DELAY_MAX_ATTEMPTS_KEY = "rsocket.server.resume.retry.fixedDelay.maxAttempts";
        String RSOCKET_SERVER_RESUME_RETRY_FIXED_DELAY_KEY = "rsocket.server.resume.retry.fixedDelay.delay";
        String RSOCKET_SERVER_RESUME_RETRY_MAX_KEY = "rsocket.server.resume.retry.max";
        String RSOCKET_SERVER_RESUME_RETRY_MAX_IN_ROW_KEY = "rsocket.server.resume.retry.maxInRow";
        String RSOCKET_SERVER_PAYLOAD_DECODER_KEY = "rsocket.server.payloadDecoder";
        String RSOCKET_SERVER_MAX_INBOUND_PAYLOAD_SIZE_KEY = "rsocket.server.maxInboundPayloadSize";
        String RSOCKET_SERVER_TRANSPORT_MODE_KEY = "rsocket.server.transport.mode";
        String RSOCKET_SERVER_TRANSPORT_PORT_KEY = "rsocket.server.transport.host";
        String RSOCKET_SERVER_TRANSPORT_HOST_KEY = "rsocket.server.transport.port";
    }

    interface Defaults {
        long DEFAULT_RETRY_MAX_ATTEMPTS = 3;
        Duration DEFAULT_RETRY_MIN_BACKOFF = Duration.ofSeconds(1);
        Duration DEFAULT_RETRY_FIXED_DELAY = Duration.ofSeconds(1);
        int DEFAULT_RETRY_MAX = 1;
        int DEFAULT_RETRY_MAX_IN_ROW = 1;
        int DEFAULT_PORT = 9000;
        Duration DEFAULT_RESUME_SESSION_DURATION = Duration.ofHours(1);
        Duration DEFAULT_RESUME_STREAM_TIMEOUT = Duration.ofHours(1);
    }


    @Getter
    @AllArgsConstructor
    enum TransportMode {
        TCP("tcp"),
        WEB_SOCKET("webSocket");

        private final String transport;

        public static TransportMode rsocketTransport(String transport) {
            if (TCP.transport.equalsIgnoreCase(transport)) return TCP;
            if (WEB_SOCKET.transport.equalsIgnoreCase(transport)) return WEB_SOCKET;
            return TCP;
        }
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

        public static RetryPolicy rsocketRetryPolicy(String policy) {
            if (BACKOFF.policy.equalsIgnoreCase(policy)) return BACKOFF;
            if (FIXED_DELAY.policy.equalsIgnoreCase(policy)) return FIXED_DELAY;
            if (MAX.policy.equalsIgnoreCase(policy)) return MAX;
            if (MAX_IN_A_ROW.policy.equalsIgnoreCase(policy)) return MAX_IN_A_ROW;
            if (INDEFINITELY.policy.equalsIgnoreCase(policy)) return INDEFINITELY;
            throw new RsocketException(format(UNSUPPORTED_RETRY_POLICY, policy));
        }
    }

    @Getter
    @AllArgsConstructor
    enum PayloadDecoderMode {
        ZERO_COPY("zeroCopy"),
        DEFAULT("default");

        private final String decoder;

        public static PayloadDecoderMode rsocketPayloadDecoder(String decoder) {
            if (ZERO_COPY.decoder.equalsIgnoreCase(decoder)) return ZERO_COPY;
            if (DEFAULT.decoder.equalsIgnoreCase(decoder)) return DEFAULT;
            return DEFAULT;
        }
    }
}
