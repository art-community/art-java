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
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.ArrayFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class ArrayTransformer implements MetaTransformer<Object[]> {
    private final Function<Integer, Object[]> arrayFactory;
    private final Function<Object, Object> parameterTransformer;

    @Override
    public List<?> toArray(Object[] value) {
        List<Object> array = dynamicArray(value.length);
        for (Object element : value) {
            array.add(let(element, parameterTransformer));
        }
        return array;
    }

    @Override
    public Object[] fromArray(List<?> value) {
        Object[] array = arrayFactory.apply(value.size());
        int index = 0;
        for (Object element : value) {
            array[index++] = let(element, parameterTransformer);
        }
        return array;
    }

    public static ArrayTransformer arrayTransformer(Function<Integer, Object[]> arrayFactory, Function<Object, Object> parameterTransformer) {
        return new ArrayTransformer(arrayFactory, parameterTransformer);
    }
}
