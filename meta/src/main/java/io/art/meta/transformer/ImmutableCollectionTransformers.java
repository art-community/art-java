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

import com.google.common.collect.*;
import io.art.core.collection.ImmutableCollection;
import io.art.core.collection.ImmutableSet;
import io.art.core.collection.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class ImmutableCollectionTransformers {
    public static MetaTransformer<ImmutableCollection<?>> immutableCollectionTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<ImmutableCollection<?>>() {
            public ImmutableCollection<?> transform(Object[] value) {
                return transform(fixedArrayOf(value));
            }

            public ImmutableCollection<?> transform(byte[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(int[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(short[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(float[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(double[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(long[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(char[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public ImmutableCollection<?> transform(List<?> value) {
                return immutableLazyArrayOf(value, parameterTransformer::transform);
            }

            public ImmutableCollection<?> transform(ImmutableList<?> value) {
                return immutableLazyArrayOf(value, parameterTransformer::transform);
            }

            public ImmutableCollection<?> transform(Iterable<?> value) {
                ImmutableArray.Builder<Object> builder = immutableArrayBuilder();
                for (Object element : value) {
                    if (isNull(element)) continue;
                    builder.add(parameterTransformer.transform(element));
                }
                return builder.build();
            }

            public ImmutableCollection<?> transform(Stream<?> value) {
                return value
                        .filter(Objects::nonNull)
                        .map(parameterTransformer::transform)
                        .collect(immutableArrayCollector());
            }

            public ImmutableCollection<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }
        };
    }

    public static MetaTransformer<ImmutableArray<?>> immutableArrayTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<ImmutableArray<?>>() {
            public ImmutableArray<?> transform(Object[] value) {
                return transform(fixedArrayOf(value));
            }

            public ImmutableArray<?> transform(byte[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(int[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(short[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(float[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(double[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(long[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(char[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public ImmutableArray<?> transform(List<?> value) {
                return immutableLazyArrayOf(value, parameterTransformer::transform);
            }

            public ImmutableArray<?> transform(ImmutableList<?> value) {
                return immutableLazyArrayOf(value, parameterTransformer::transform);
            }

            public ImmutableArray<?> transform(Iterable<?> value) {
                ImmutableArray.Builder<Object> builder = immutableArrayBuilder();
                for (Object element : value) {
                    if (isNull(element)) continue;
                    builder.add(parameterTransformer.transform(element));
                }
                return builder.build();
            }

            public ImmutableArray<?> transform(Stream<?> value) {
                return value
                        .filter(Objects::nonNull)
                        .map(parameterTransformer::transform)
                        .collect(immutableArrayCollector());
            }

            public ImmutableArray<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }
        };
    }

    public static MetaTransformer<ImmutableSet<?>> immutableSetTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<ImmutableSet<?>>() {
            public ImmutableSet<?> transform(Iterable<?> value) {
                ImmutableSet.Builder<Object> builder = immutableSetBuilder();
                for (Object element : value) {
                    if (isNull(element)) continue;
                    builder.add(parameterTransformer.transform(element));
                }
                return builder.build();
            }

            public ImmutableSet<?> transform(byte[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(int[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(short[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(float[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(double[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(long[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(char[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public ImmutableSet<?> transform(Stream<?> value) {
                return value
                        .filter(Objects::nonNull)
                        .map(parameterTransformer::transform)
                        .collect(immutableSetCollector());
            }

            public ImmutableSet<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }
        };
    }
}
