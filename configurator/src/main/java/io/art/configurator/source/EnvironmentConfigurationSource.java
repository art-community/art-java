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

package io.art.configurator.source;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.combiner.SectionCombiner.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.handler.ExceptionHandler.*;
import static java.lang.Integer.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@Getter
@Public
@AllArgsConstructor
public class EnvironmentConfigurationSource implements NestedConfiguration {
    private final String section;
    private final ModuleConfigurationSourceType type = ENVIRONMENT;
    private final String path = type.toString();
    private final ImmutableMap<String, String> environment = context().configuration().getEnvironment();

    @Override
    public ImmutableSet<String> getKeys() {
        if (isEmpty(section)) {
            return immutableSetOf(environment.keySet());
        }
        return environment.keySet()
                .stream()
                .filter(key -> key.equals(section) || key.startsWith(section + UNDERSCORE))
                .collect(immutableSetCollector());
    }

    @Override
    public NestedConfiguration getNested(String path) {
        String newSection = combine(section, path).replaceAll(ESCAPED_DOT, UNDERSCORE);
        ImmutableSet<String> keys = getKeys();
        for (String key : keys) {
            if (key.startsWith(newSection)) {
                return new EnvironmentConfigurationSource(newSection);
            }
        }
        return null;
    }

    @Override
    public String dump() {
        return environment.entrySet()
                .stream()
                .map(entry -> entry.getKey() + EQUAL + entry.getValue())
                .collect(joining(NEW_LINE));
    }

    @Override
    public Boolean asBoolean() {
        return let(environment.get(section), Boolean::valueOf);
    }

    @Override
    public String asString() {
        return environment.get(section);
    }

    @Override
    public ImmutableArray<NestedConfiguration> asArray() {
        ImmutableSet<String> keys = getKeys();
        Map<Integer, NestedConfiguration> array = map();
        for (String key : keys) {
            String arrayPrefix = isEmpty(section) ? key : key.substring(key.indexOf(section) + section.length() + 1);
            Integer index = nullIfException(() -> parseInt(arrayPrefix.contains(UNDERSCORE) ? arrayPrefix.substring(0, arrayPrefix.indexOf(UNDERSCORE)) : arrayPrefix));
            if (nonNull(index) && index >= 0) {
                array.put(index, new EnvironmentConfigurationSource(combine(section, index.toString()).replaceAll(ESCAPED_DOT, UNDERSCORE)));
            }
        }
        return array.keySet().stream().sorted().map(array::get).collect(immutableArrayCollector());
    }

    @Override
    public <T> ImmutableArray<T> asArray(Function<NestedConfiguration, T> mapper) {
        return asArray().stream().map(mapper).collect(immutableArrayCollector());
    }
}
