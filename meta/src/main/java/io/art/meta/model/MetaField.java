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

package io.art.meta.model;

import io.art.core.annotation.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static java.util.Objects.*;
import static lombok.EqualsAndHashCode.CacheStrategy.*;

@ToString
@Generation
@EqualsAndHashCode(exclude = "owner", cacheStrategy = LAZY)
public class MetaField<OwnerType extends MetaClass<?>, FieldType> {
    private final String name;
    private final MetaType<?> type;
    private final boolean inherited;
    private final OwnerType owner;
    private Boolean known;

    public MetaField(String name, MetaType<?> type, boolean inherited, OwnerType owner) {
        this.name = name;
        this.owner = owner;
        this.type = type;
        this.inherited = inherited;
    }

    public String name() {
        return name;
    }

    public boolean inherited() {
        return inherited;
    }

    public MetaType<FieldType> type() {
        return cast(type);
    }

    public boolean isKnown() {
        if (nonNull(known)) return known;
        known = true;
        return known = type.isKnown();
    }

    public OwnerType owner() {
        return owner;
    }
}
