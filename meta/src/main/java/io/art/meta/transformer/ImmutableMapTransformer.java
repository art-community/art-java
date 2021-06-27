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
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.MapFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class ImmutableMapTransformer implements MetaTransformer<ImmutableMap<?, ?>> {
    private final Function<Object, Object> keyTransformer;
    private final Function<Object, Object> valueTransformer;

    @Override
    public ImmutableMap<?, ?> fromMap(Map<?, ?> value) {
        Map<Object, Object> transformedKeys = map(value.size());
        for (Object key : value.keySet()) {
            transformedKeys.put(let(key, keyTransformer), key);
        }
        return immutableLazyMap(transformedKeys.keySet(), key -> let(value.get(transformedKeys.get(key)), valueTransformer));
    }

    @Override
    public Map<?, ?> toMap(ImmutableMap<?, ?> value) {
        Map<Object, Object> map = map(value.size());
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            map.put(let(entry.getKey(), keyTransformer), let(entry.getValue(), valueTransformer));
        }
        return map;
    }

    public static ImmutableMapTransformer immutableMapTransformer(Function<Object, Object> keyTransformer, Function<Object, Object> valueTransformer) {
        return new ImmutableMapTransformer(keyTransformer, valueTransformer);
    }
}
