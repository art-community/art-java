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
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.QueueFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class QueueTransformer implements MetaTransformer<Queue<?>> {
    private final Function<Object, Object> parameterTransformer;

    @Override
    public Queue<?> fromArray(List<?> value) {
        Queue<Object> queue = queue();
        for (Object element : value) {
            queue.add(parameterTransformer.apply(element));
        }
        return queue;
    }

    @Override
    public List<?> toArray(Queue<?> value) {
        List<Object> list = dynamicArray(value.size());
        for (Object element : value) {
            list.add(parameterTransformer.apply(element));
        }
        return list;
    }

    public static QueueTransformer queueTransformer(Function<Object, Object> parameterTransformer) {
        return new QueueTransformer(parameterTransformer);
    }
}
