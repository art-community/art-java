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
import static io.art.core.caster.Caster.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public class ByteArrayTransformer implements MetaTransformer<byte[]> {
    @Override
    public byte[] fromArray(List<?> value) {
        byte[] bytes = new byte[value.size()];
        for (int i = 0; i < value.size(); i++) {
            Byte element = cast(value.get(i));
            if (nonNull(element)) {
                bytes[i] = element;
            }
        }
        return bytes;
    }

    @Override
    public List<?> toArray(byte[] value) {
        return fixedArrayOf(box(value));
    }

    @Override
    public byte[] fromByteArray(byte[] value) {
        return value;
    }

    @Override
    public byte[] toByteArray(byte[] value) {
        return value;
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(byte[] value) {
        return cast(immutableLazyArray(index -> value[index], value.length));
    }

    @Override
    public byte[] fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return unbox(value.toArray(new Byte[value.size()]));
    }

    @Override
    public String toString(byte[] value) {
        return new String(value, context().configuration().getCharset());
    }

    @Override
    public byte[] fromString(String value) {
        return value.getBytes(context().configuration().getCharset());
    }

    public static ByteArrayTransformer BYTE_ARRAY_TRANSFORMER = new ByteArrayTransformer();
}
