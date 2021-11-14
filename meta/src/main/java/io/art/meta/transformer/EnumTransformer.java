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
import static lombok.AccessLevel.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class EnumTransformer implements MetaTransformer<Enum<?>> {
    private final Function<String, Enum<?>> enumFactory;

    @Override
    public String toString(Enum<?> value) {
        return value.name();
    }

    @Override
    public Enum<?> fromString(String value) {
        return enumFactory.apply(value);
    }

    public static EnumTransformer enumTransformer(Function<String, Enum<?>> enumFactory) {
        return new EnumTransformer(enumFactory);
    }
}
