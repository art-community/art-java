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

import io.art.core.collection.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@AllArgsConstructor(access = PRIVATE)
public class FluxTransformer implements MetaTransformer<Flux<?>> {
    @Override
    public Flux<?> fromArray(List<?> value) {
        return fromIterable(value);
    }

    @Override
    public List<?> toArray(Flux<?> value) {
        return dynamicArrayOf(value.toIterable());
    }

    @Override
    public Flux<?> fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return Flux.fromStream(value.stream());
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(Flux<?> value) {
        List<?> array = dynamicArrayOf(value.toIterable());
        return cast(immutableLazyArrayOf(array, identity()));
    }

    public static FluxTransformer FLUX_TRANSFORMER = new FluxTransformer();
}
