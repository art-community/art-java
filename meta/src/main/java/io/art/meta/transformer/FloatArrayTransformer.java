/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public class FloatArrayTransformer implements MetaTransformer<float[]> {
    @Override
    public float[] fromArray(List<?> value) {
        float[] floats = new float[value.size()];
        for (int i = 0; i < value.size(); i++) {
            Float element = cast(value.get(i));
            if (nonNull(element)) {
                floats[i] = element;
            }
        }
        return floats;
    }

    @Override
    public List<?> toArray(float[] value) {
        return fixedArrayOf(box(value));
    }


    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(float[] value) {
        return cast(immutableLazyArray(index -> value[index], value.length));
    }

    @Override
    public float[] fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return unbox(value.toArray(new Float[value.size()]));
    }

    public static FloatArrayTransformer FLOAT_ARRAY_TRANSFORMER = new FloatArrayTransformer();
}
