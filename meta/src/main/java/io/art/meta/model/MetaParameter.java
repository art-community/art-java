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

package io.art.meta.model;

import io.art.core.annotation.*;
import io.art.core.caster.*;
import lombok.*;
import lombok.experimental.*;
import java.util.*;

@ToString
@ForGenerator
@EqualsAndHashCode
@AllArgsConstructor
public class MetaParameter<T> {
    private final int index;
    private final String name;
    private final MetaType<?> type;

    public int index() {
        return index;
    }

    public String name() {
        return name;
    }

    public MetaType<T> type() {
        return Caster.cast(type);
    }
}
