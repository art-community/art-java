/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.*;

@AllArgsConstructor(access = PRIVATE)
public class ImmutableSetTransformer implements MetaTransformer<ImmutableSet<?>> {

    @Override
    public ImmutableSet<?> fromArray(List<?> value) {
        return immutableSetOf(value);
    }

    @Override
    public List<?> toArray(ImmutableSet<?> value) {
        return fixedArrayOf(value.toMutable());
    }

    @Override
    public ImmutableSet<?> fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return value.asSet();
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(ImmutableSet<?> value) {
        return cast(immutableLazyArrayOf(value.asArray(), identity()));
    }

    public static ImmutableSetTransformer IMMUTABLE_SET_TRANSFORMER = new ImmutableSetTransformer();
}
