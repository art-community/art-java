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

import io.art.core.property.*;
import static io.art.core.property.LazyProperty.*;
import java.util.*;
import java.util.function.*;

public class SpecialTransformers {
    public static MetaTransformer<LazyProperty<?>> lazyTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<>() {
            @Override
            public LazyProperty<?> transform(Object value) {
                return lazy(() -> parameterTransformer.transform(value));
            }

            public LazyProperty<?> transform(LazyProperty<?> value) {
                return value;
            }
        };
    }

    public static MetaTransformer<Supplier<?>> supplierTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<>() {
            @Override
            public Supplier<?> transform(Object value) {
                return () -> parameterTransformer.transform(value);
            }

            public Supplier<?> transform(Supplier<?> value) {
                return value;
            }
        };
    }

    public static MetaTransformer<Optional<?>> optionalTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<>() {
            @Override
            public Optional<?> transform(Object value) {
                return Optional.ofNullable(parameterTransformer.transform(value));
            }

            public Optional<?> transform(Optional<?> value) {
                return value;
            }
        };
    }
}
