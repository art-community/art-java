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

package io.art.transport.payload;

import io.art.core.property.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.publisher.*;

@AllArgsConstructor
public class TransportPayload {
    @Getter
    private final ByteBuf data;

    private final LazyProperty<?> value;

    private static final TransportPayload EMPTY = new TransportPayload(null, null);
    private static final Flux<TransportPayload> EMPTY_TRANSPORT_FLUX = Flux.just(EMPTY);
    private static final Mono<TransportPayload> EMPTY_TRANSPORT_MONO = Mono.just(EMPTY);

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public static TransportPayload emptyTransportPayload() {
        return EMPTY;
    }

    public static Flux<TransportPayload> emptyTransportPayloadFlux() {
        return EMPTY_TRANSPORT_FLUX;
    }

    public static Mono<TransportPayload> emptyTransportPayloadMono() {
        return EMPTY_TRANSPORT_MONO;
    }

    public Object getValue() {
        return value.get();
    }
}
