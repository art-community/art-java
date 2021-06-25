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

import lombok.experimental.*;
import static io.art.core.extensions.ArrayExtensions.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class ArrayTransformers {
    public static MetaTransformer<byte[]> BYTE_ARRAY_TRANSFORMER = new MetaTransformer<byte[]>() {
        public byte[] transform(byte[] value) {
            return value;
        }

        public byte[] transform(List<Byte> value) {
            return unbox(value.toArray(new Byte[0]));
        }
    };

    public static MetaTransformer<Object[]> arrayTransformer(Function<Integer, ?> arrayFactory, MetaTransformer<Object[]> parameterTransformer) {
        return new MetaTransformer<Object[]>() {
            public Object[] transform(Object[] value) {
                return value;
            }

            public Object[] transform(List<?> value) {
                Object[] array = (Object[]) arrayFactory.apply(value.size());
                for (int index = 0, valueSize = value.size(); index < valueSize; index++) {
                    array[index] = parameterTransformer.transform(value.get(index));
                }
                return array;
            }
        };
    }
}
