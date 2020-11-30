/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.core.collector;

import lombok.experimental.*;
import static io.art.core.constants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@UtilityClass
public class MapCollectors {
    public static <T, K, V> Collector<T, ?, Map<K, V>> mapCollector(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
        BinaryOperator<V> mergeFunction = (key, value) -> {
            throw new IllegalStateException(format(DUPLICATE_KEY, key));
        };
        return toMap(keyFunction, valueFunction, mergeFunction, LinkedHashMap::new);
    }
}
