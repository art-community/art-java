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
import static io.art.core.collector.ArrayCollector.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@AllArgsConstructor(access = PRIVATE)
public class StreamTransformer implements MetaTransformer<Stream<?>> {
    private final Function<Object, Object> parameterTransformer;

    @Override
    public Stream<?> fromArray(List<?> value) {
        return value.stream().map(parameterTransformer);
    }

    @Override
    public List<?> toArray(Stream<?> value) {
        return value.map(parameterTransformer).collect(arrayCollector());
    }

    public static StreamTransformer streamTransformer(Function<Object, Object> parameterTransformer) {
        return new StreamTransformer(parameterTransformer);
    }
}
