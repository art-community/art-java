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
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class ArrayTransformer implements MetaTransformer<Object[]> {
    private final Function<Integer, Object[]> arrayFactory;

    @Override
    public List<?> toArray(Object[] value) {
        return fixedArrayOf(value);
    }

    @Override
    public Object[] fromArray(List<?> value) {
        Object[] array = arrayFactory.apply(value.size());
        int index = 0;
        for (Object element : value) {
            array[index++] = element;
        }
        return array;
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(Object[] value) {
        return cast(immutableLazyArray(index -> value[index], value.length));
    }

    @Override
    public Object[] fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return value.toArray(arrayFactory.apply(value.size()));
    }

    public static ArrayTransformer arrayTransformer(Function<Integer, Object[]> arrayFactory) {
        return new ArrayTransformer(arrayFactory);
    }
}
