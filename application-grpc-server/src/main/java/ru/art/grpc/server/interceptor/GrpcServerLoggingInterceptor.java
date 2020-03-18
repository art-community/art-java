/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.grpc.server.interceptor;

import io.grpc.ForwardingServerCall.*;
import io.grpc.ForwardingServerCallListener.*;
import io.grpc.*;
import io.grpc.ServerCall.*;
import org.apache.logging.log4j.Logger;
import ru.art.grpc.server.module.GrpcServerModule;
import ru.art.logging.*;
import static io.grpc.Metadata.*;
import static io.grpc.Metadata.Key.*;
import static java.lang.System.*;
import static java.text.MessageFormat.*;
import static java.util.UUID.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.grpc.server.constants.GrpcServerLoggingMessages.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.grpc.server.module.GrpcServerModule.grpcServerModule;
import static ru.art.logging.LoggingModule.*;
import static ru.art.logging.LoggingParametersManager.*;

public class GrpcServerLoggingInterceptor implements ServerInterceptor {
    private final Logger logger = loggingModule().getLogger(GrpcServerLoggingInterceptor.class);

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String traceIdHeader = metadata.get(of(TRACE_ID_HEADER, ASCII_STRING_MARSHALLER));
        String profileHeader = metadata.get(of(PROFILE_HEADER, ASCII_STRING_MARSHALLER));
        clearProtocolLoggingParameters();
        putProtocolCallLoggingParameters(ProtocolCallLoggingParameters.builder()
                .profile(profileHeader)
                .environment(getProperty(ENVIRONMENT_PROPERTY))
                .protocol(GRPC_SERVICE_TYPE)
                .requestId(randomUUID().toString())
                .traceId(isEmpty(traceIdHeader) ? randomUUID().toString() : traceIdHeader)
                .build());
        if (!grpcServerModule().isEnableRawDataTracing()) {
            return serverCallHandler.startCall(serverCall, metadata);
        }
        logger.info(format(GRPC_ON_REQUEST_HEADERS, emptyIfNull(metadata.toString())));
        return new SimpleForwardingServerCallListener<ReqT>(serverCallHandler.startCall(new SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
            @Override
            public void sendMessage(RespT message) {
                logger.info(format(GRPC_ON_RESPONSE_MESSAGE, emptyIfNull(message)));
                super.sendMessage(message);
            }

            @Override
            public void sendHeaders(Metadata headers) {
                logger.info(format(GRPC_ON_RESPONSE_HEADERS, emptyIfNull(metadata.toString())));
                super.sendHeaders(headers);
            }

            @Override
            public void close(Status status, Metadata trailers) {
                logger.info(format(GRPC_ON_CLOSE, emptyIfNull(metadata.toString())));
                super.close(status, trailers);
            }
        }, metadata)) {
            @Override
            public void onMessage(ReqT message) {
                logger.info(format(GRPC_ON_REQUEST_MESSAGE, emptyIfNull(message)));
                super.onMessage(message);
            }

            @Override
            public void onHalfClose() {
                logger.info(GRPC_ON_HALF_CLOSE);
                super.onHalfClose();
            }

            @Override
            public void onCancel() {
                logger.info(GRPC_ON_CANCEL);
                super.onCancel();
            }

            @Override
            public void onComplete() {
                logger.info(GRPC_ON_COMPLETE);
                super.onComplete();
            }

            @Override
            public void onReady() {
                logger.info(GRPC_ON_READY);
                super.onReady();
            }
        };
    }
}
