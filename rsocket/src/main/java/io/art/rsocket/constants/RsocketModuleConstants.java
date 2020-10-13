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

import lombok.*;

public interface RsocketModuleConstants {
    String RSOCKET_MODULE_ID = "RSOCKET_MODULE";
    String RSOCKET_FUNCTION_SERVICE = "RSOCKET_FUNCTION_SERVICE";
    String REQUEST_DATA = "requestData";
    String RSOCKET_SERVICE_TYPE = "RSOCKET_SERVICE";
    String RSOCKET_COMMUNICATION_SERVICE_TYPE = "RSOCKET_COMMUNICATION";
    String RSOCKET_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND = "RSocket communication target configuration was not found for serviceId: ''{0}''";
    String RSOCKET_CLIENT_DISPOSING = "Disposing RSocket client";
    int DEFAULT_RSOCKET_TCP_PORT = 9000;
    int DEFAULT_RSOCKET_WEB_SOCKET_PORT = 10000;
    long DEFAULT_RSOCKET_RESUME_SESSION_DURATION = 24 * 60 * 60 * 1000;
    long DEFAULT_RSOCKET_RESUME_STREAM_TIMEOUT = 24 * 60 * 60 * 1000;

    enum RsocketTransport {
        TCP,
        WEB_SOCKET
    }

    interface ExceptionMessages {
        String SPECIFICATION_NOT_FOUND = "Setup payload was null or not contained serviceId, methodId. Default service method id was not specified in configuration";
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
        String RSOCKET_LOADED_SERVICE_MESSAGE = "RSocket service loaded: {0}:{1,number,#} - {2}.{3}";
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
        String RSOCKET_RESUME_SECTION = "rsocket.server.resume";
        String RSOCKET_RESUME_CLEANUP_STORE_ON_KEEP_ALIVE = "rsocket.server.resume.cleanupStoreOnKeepAlive";
        String RSOCKET_RESUME_SESSION_DURATION = "rsocket.server.resume.sessionDuration";
        String RSOCKET_RESUME_STREAM_TIMEOUT = "rsocket.server.resume.streamTimeout";
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
    }
}
