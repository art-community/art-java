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

package io.art.meta.transformer;

import org.reactivestreams.*;
import reactor.core.publisher.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ReactiveTransformers {
    public static MetaTransformer<Mono<?>> monoTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<Mono<?>>() {
            @Override
            public Mono<?> transform(Object value) {
                return Mono.justOrEmpty(parameterTransformer.transform(value));
            }

            @Override
            public Mono<?> transform(Optional<?> value) {
                return Mono.justOrEmpty(value);
            }

            public Mono<?> transform(Supplier<?> value) {
                return Mono.fromSupplier(value).map(parameterTransformer::transform);
            }

            public Mono<?> transform(Publisher<?> value) {
                return Mono.from(value).map(parameterTransformer::transform);
            }

            public Mono<?> transform(Mono<?> value) {
                return value.map(parameterTransformer::transform);
            }
        };
    }

    public static MetaTransformer<Flux<?>> fluxTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<Flux<?>>() {
            @Override
            public Flux<?> transform(Object value) {
                return Flux.just(parameterTransformer.transform(value));
            }

            public Flux<?> transform(Stream<?> value) {
                return Flux.fromStream(value).map(parameterTransformer::transform);
            }

            public Flux<?> transform(Iterable<?> value) {
                return Flux.fromIterable(value).map(parameterTransformer::transform);
            }

            public Flux<?> transform(Object[] value) {
                return Flux.fromArray(value).map(parameterTransformer::transform);
            }

            public Flux<?> transform(Publisher<?> value) {
                return Flux.from(value).map(parameterTransformer::transform);
            }

            public Flux<?> transform(Flux<?> value) {
                return value.map(parameterTransformer::transform);
            }
        };
    }
}
