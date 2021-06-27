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

import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ArrayFactory.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class FluxTransformer implements MetaTransformer<Flux<?>> {
    private final Function<Object, Object> parameterTransformer;

    @Override
    public Flux<?> fromArray(List<?> value) {
        return fromIterable(value).map(parameterTransformer);
    }

    @Override
    public List<?> toArray(Flux<?> value) {
        return fixedArrayOf(value.map(parameterTransformer).toIterable());
    }

    public static FluxTransformer fluxTransformer(Function<Object, Object> parameterTransformer) {
        return new FluxTransformer(parameterTransformer);
    }
}
