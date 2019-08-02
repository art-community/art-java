/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.tarantool.configuration.lua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jtwig.JtwigModel;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.tarantool.constants.TarantoolModuleConstants.JTW_EXTENSION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.CREATE_SEQUENCE;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.SEQUENCE_MANAGEMENT;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "tarantoolSequence")
@SuppressWarnings("Duplicates")
public class TarantoolSequenceConfiguration {
    private final String sequenceName;
    private String start;
    private String min;
    private String max;
    private String cycle;
    private String cache;
    private String step;

    public String toCreateSequenceLua() {
        return classpathTemplate(CREATE_SEQUENCE + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(SEQUENCE_NAME, sequenceName)
                        .with(START, start)
                        .with(MIN, min)
                        .with(MAX, max)
                        .with(CYCLE, cycle)
                        .with(CACHE, cache)
                        .with(STEP, step));
    }

    public String toManageSequenceLua() {
        return classpathTemplate(SEQUENCE_MANAGEMENT + JTW_EXTENSION)
                .render(new JtwigModel().with(SEQUENCE_NAME, sequenceName));
    }
}
