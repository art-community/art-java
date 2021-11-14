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
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.SetFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class SetTransformer implements MetaTransformer<Set<?>> {
    @Override
    public Set<?> fromArray(List<?> value) {
        return setOf(value);
    }

    @Override
    public List<?> toArray(Set<?> value) {
        return fixedArrayOf(value);
    }

    @Override
    public Set<?> fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return setOf(value);
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(Set<?> value) {
        List<?> array = fixedArrayOf(value);
        return cast(immutableLazyArrayOf(array, Function.identity()));
    }

    public static SetTransformer SET_TRANSFORMER = new SetTransformer();
}
