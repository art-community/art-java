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

package ru.art.rsocket.interceptor;

import io.rsocket.*;
import io.rsocket.plugins.*;
import io.rsocket.util.*;
import org.apache.logging.log4j.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.util.annotation.*;
import static java.text.MessageFormat.*;
import static reactor.core.publisher.Flux.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;

public class RsocketLoggingInterceptor implements RSocketInterceptor {
    private final static Logger logger = loggingModule().getLogger(RSocketInterceptor.class);

    @Override
    public RSocket apply(RSocket rsocket) {
        return new RSocketProxy(rsocket) {
            @Override
            public Mono<Void> fireAndForget(@NonNull Payload payload) {
                logger.info(format(RSOCKET_FIRE_AND_FORGET_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                return super.fireAndForget(payload)
                        .doOnSubscribe(nothing -> logger.info(RSOCKET_FIRE_AND_FORGET_RESPONSE_LOG))
                        .doOnError(error -> logger.error(format(RSOCKET_FIRE_AND_FORGET_EXCEPTION_LOG, error)));
            }

            @Override
            public Mono<Payload> requestResponse(@NonNull Payload payload) {
                logger.info(format(RSOCKET_REQUEST_RESPONSE_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                return super.requestResponse(payload)
                        .doOnNext(response ->
                                logger.info(format(RSOCKET_REQUEST_RESPONSE_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())))
                        .doOnError(error -> logger.error(format(RSOCKET_REQUEST_RESPONSE_EXCEPTION_LOG, error)));
            }

            @Override
            public Flux<Payload> requestStream(@NonNull Payload payload) {
                logger.info(format(RSOCKET_REQUEST_STREAM_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                return super.requestStream(payload)
                        .doOnNext(response ->
                                logger.info(format(RSOCKET_REQUEST_STREAM_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())))
                        .doOnError(error -> logger.error(format(RSOCKET_REQUEST_STREAM_EXCEPTION_LOG, error)));
            }

            @Override
            public Flux<Payload> requestChannel(@NonNull Publisher<Payload> payloads) {
                return super.requestChannel(from(payloads)
                        .doOnNext(payload -> logger
                                .info(format(RSOCKET_REQUEST_CHANNEL_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()))))
                        .doOnNext(response ->
                                logger.info(format(RSOCKET_REQUEST_CHANNEL_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())))
                        .doOnError(error -> logger.error(format(RSOCKET_REQUEST_CHANNEL_EXCEPTION_LOG, error)));
            }

            @Override
            public Mono<Void> metadataPush(@NonNull Payload payload) {
                logger.info(format(RSOCKET_METADATA_PUSH_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                return super.fireAndForget(payload)
                        .doOnSubscribe(nothing -> logger.info(RSOCKET_METADATA_PUSH_RESPONSE_LOG))
                        .doOnError(error -> logger.error(format(RSOCKET_METADATA_PUSH_EXCEPTION_LOG, error)));
            }
        };
    }
}
