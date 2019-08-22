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

package ru.art.grpc.client.interceptor;

import io.grpc.*;
import org.apache.logging.log4j.*;
import javax.annotation.*;

import static java.text.MessageFormat.*;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.*;
import static ru.art.logging.LoggingModule.*;

public class GrpcClientLoggingInterceptor implements ClientInterceptor {

    private final Logger logger = loggingModule()
            .getLogger(GrpcClientLoggingInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void sendMessage(ReqT message) {
                logger.info(format(GRPC_ON_REQUEST_MESSAGE, message));
                super.sendMessage(message);
            }

            @Override
            public void cancel(@Nullable String message, @Nullable Throwable cause) {
                logger.error(format(GRPC_ON_CANCEL, message, cause));
                super.cancel(message, cause);
            }

            @Override
            public void halfClose() {
                logger.info(GRPC_ON_HALF_CLOSE);
                super.halfClose();
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(new Listener<RespT>() {
                    @Override
                    public void onHeaders(Metadata headers) {
                        logger.info(format(GRPC_ON_RESPONSE_HEADERS, headers.toString()));
                        responseListener.onHeaders(headers);
                    }

                    @Override
                    public void onMessage(RespT message) {
                        logger.info(format(GRPC_ON_RESPONSE_MESSAGE, message));
                        responseListener.onMessage(message);
                    }

                    @Override
                    public void onClose(Status status, Metadata trailers) {
                        logger.info(format(GRPC_ON_CLOSE, status.toString(), trailers.toString()));
                        responseListener.onClose(status, trailers);
                    }

                    @Override
                    public void onReady() {
                        logger.info(GRPC_ON_READY);
                        responseListener.onReady();
                    }
                }, headers);
            }
        };
    }
}
