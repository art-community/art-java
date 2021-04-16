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

package io.art.model.customizer;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.value.configuration.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import io.art.value.registry.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.value.mapper.ValueMapper.*;
import static java.util.Map.*;
import java.lang.reflect.*;
import java.util.*;

@UsedByGenerator
public class ValueCustomizer {
    @Getter
    private final Custom configuration = new Custom();

    public ValueCustomizer registry(ValueMapperRegistry registry) {
        configuration.registry = registry;
        return this;
    }

    @Getter
    private static class Custom extends ValueModuleConfiguration {
        private ValueMapperRegistry registry = new ValueMapperRegistry();
    }
}
