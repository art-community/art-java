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
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.factory.ArrayFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class ImmutableCollectionTransformer implements MetaTransformer<ImmutableCollection<?>> {
    private final Function<Object, Object> parameterTransformer;

    @Override
    public ImmutableCollection<?> fromArray(List<?> value) {
        ImmutableArray.Builder<Object> builder = immutableArrayBuilder();
        for (Object element : value) {
            builder.add(parameterTransformer.apply(element));
        }
        return builder.build();
    }

    @Override
    public List<?> toArray(ImmutableCollection<?> value) {
        List<Object> list = dynamicArray(value.size());
        for (Object element : value) {
            list.add(parameterTransformer.apply(element));
        }
        return list;
    }

    public static ImmutableCollectionTransformer immutableCollectionTransformer(Function<Object, Object> parameterTransformer) {
        return new ImmutableCollectionTransformer(parameterTransformer);
    }
}
