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
public class CharacterArrayTransformer implements MetaTransformer<char[]> {
    @Override
    public char[] fromArray(List<?> value) {
        char[] chars = new char[value.size()];
        for (int i = 0; i < value.size(); i++) {
            Character element = cast(value.get(i));
            if (nonNull(element)) {
                chars[i] = element;
            }
        }
        return chars;
    }

    @Override
    public List<?> toArray(char[] value) {
        return fixedArrayOf(box(value));
    }

    @Override
    public String toString(char[] value) {
        return new String(value);
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(char[] value) {
        return cast(immutableLazyArray(index -> value[index], value.length));
    }

    @Override
    public char[] fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return unbox(value.toArray(new Character[value.size()]));
    }

    @Override
    public char[] fromString(String value) {
        return value.toCharArray();
    }

    public static CharacterArrayTransformer CHARACTER_ARRAY_TRANSFORMER = new CharacterArrayTransformer();
}
