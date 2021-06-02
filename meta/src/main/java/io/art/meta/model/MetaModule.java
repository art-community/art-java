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
import io.art.core.collection.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static java.util.Arrays.*;
import java.util.*;

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaModule {
    private final Map<String, MetaPackage> packages = map();

    protected <T extends MetaPackage> T register(T metaPackage) {
        packages.put(metaPackage.name(), metaPackage);
        return metaPackage;
    }

    protected void compute(MetaModule... dependencies) {
        stream(dependencies).forEach(MetaModule::compute);
        packages.values().forEach(MetaPackage::compute);
    }

    public ImmutableMap<String, MetaPackage> packages() {
        return immutableMapOf(packages);
    }

    public <T extends MetaPackage> T packageOf(String name) {
        return cast(packages.get(name));
    }
}
