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
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class ImmutableMapTransformers {
    public static MetaTransformer<ImmutableMap<?, ?>> immutableMapTransformer(MetaTransformer<?> keyTransformer, MetaTransformer<?> valueTransformer) {
        return new MetaTransformer<ImmutableMap<?, ?>>() {
            public ImmutableMap<?, ?> transform(ImmutableMap<?, ?> value) {
                Set<?> keys = value.keySet().stream().filter(Objects::nonNull).map(keyTransformer::transform).collect(setCollector());
                return immutableLazyMap(keys, mapValue -> let(mapValue, notNull -> cast(valueTransformer.transform(notNull))));
            }

            public ImmutableMap<?, ?> transform(Map<?, ?> value) {
                Set<?> keys = value.keySet().stream().filter(Objects::nonNull).map(keyTransformer::transform).collect(setCollector());
                return immutableLazyMap(keys, mapValue -> let(mapValue, notNull -> cast(valueTransformer.transform(notNull))));
            }
        };
    }
}
