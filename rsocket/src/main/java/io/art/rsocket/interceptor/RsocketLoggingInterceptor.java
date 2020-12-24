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

package io.art.rsocket.interceptor;

import io.rsocket.*;
import io.rsocket.plugins.*;
import io.rsocket.util.*;
import lombok.*;
import org.apache.logging.log4j.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.util.annotation.NonNull;
import static com.google.common.base.Throwables.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketLoggingInterceptor implements RSocketInterceptor {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketLoggingInterceptor.class);
    private final Supplier<Boolean> enabled;

    @Override
    public RSocket apply(RSocket rsocket) {
        return new RSocketProxy(rsocket) {
            private final Logger logger = getLogger();

            @Override
            public Mono<Void> fireAndForget(@NonNull Payload payload) {
                if (!enabled.get()) {
                    return super.fireAndForget(payload);
                }
                logger.info(format(FIRE_AND_FORGET_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                Mono<Void> output = super.fireAndForget(payload).doOnError(error -> logger.error(format(FIRE_AND_FORGET_EXCEPTION_LOG, getStackTraceAsString(error))));
                return output.doOnNext(nothing -> logger.info(FIRE_AND_FORGET_RESPONSE_LOG));
            }

            @Override
            public Mono<Payload> requestResponse(@NonNull Payload payload) {
                if (!enabled.get()) {
                    return super.requestResponse(payload);
                }
                logger.info(format(REQUEST_RESPONSE_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                Mono<Payload> output = super.requestResponse(payload).doOnError(error -> logger.error(format(REQUEST_RESPONSE_EXCEPTION_LOG, getStackTraceAsString(error))));
                return output.doOnNext(response -> logger.info(format(RESPONSE_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())));
            }

            @Override
            public Flux<Payload> requestStream(@NonNull Payload payload) {
                if (!enabled.get()) {
                    return super.requestStream(payload);
                }
                logger.info(format(REQUEST_STREAM_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                Flux<Payload> output = super.requestStream(payload).doOnError(error -> logger.error(format(REQUEST_STREAM_EXCEPTION_LOG, getStackTraceAsString(error))));
                return output.doOnNext(response -> logger.info(format(REQUEST_STREAM_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())));
            }

            @Override
            public Flux<Payload> requestChannel(@NonNull Publisher<Payload> payloads) {
                if (!enabled.get()) {
                    return super.requestChannel(payloads);
                }
                Flux<Payload> input = from(payloads)
                        .doOnNext(payload -> logger.info(format(REQUEST_CHANNEL_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8())))
                        .doOnError(error -> logger.error(format(REQUEST_CHANNEL_EXCEPTION_LOG, getStackTraceAsString(error))));
                Flux<Payload> output = super.requestChannel(input);
                return output.doOnNext(payload -> logger.info(format(REQUEST_CHANNEL_RESPONSE_LOG, payload.getDataUtf8(), payload.getMetadataUtf8())));
            }

            @Override
            public Mono<Void> metadataPush(@NonNull Payload payload) {
                if (!enabled.get()) {
                    return super.metadataPush(payload);
                }
                logger.info(format(METADATA_PUSH_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
                Mono<Void> output = super.metadataPush(payload).doOnError(error -> logger.error(format(METADATA_PUSH_EXCEPTION_LOG, getStackTraceAsString(error))));
                return output.doOnNext(nothing -> logger.info(METADATA_PUSH_RESPONSE_LOG));
            }
        };
    }
}
