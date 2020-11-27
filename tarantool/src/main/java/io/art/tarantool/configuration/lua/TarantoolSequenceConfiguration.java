/*
 * ART Java
 *
 * Copyright 2019 ART
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

package io.art.tarantool.configuration.lua;

import com.mitchellbosecke.pebble.*;
import com.mitchellbosecke.pebble.loader.*;
import io.art.tarantool.exception.*;
import lombok.*;
import static io.art.core.builder.MapBuilder.*;
import static io.art.core.caster.Caster.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Templates.*;
import java.io.*;
import java.util.*;

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
        Map<String, Object> templateContext = cast(mapBuilder()
                .with(SEQUENCE_NAME, sequenceName)
                .with(START, start)
                .with(MIN, min)
                .with(MAX, max)
                .with(CYCLE, cycle)
                .with(CACHE, cache)
                .with(STEP, step));
        StringWriter templateWriter = new StringWriter();
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(CREATE_SEQUENCE + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

    public String toManageSequenceLua() {
        StringWriter templateWriter = new StringWriter();
        try {
            Map<String, Object> builder = cast(mapBuilder().with(SEQUENCE_NAME, sequenceName).build());
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(SEQUENCE_MANAGEMENT + TWIG_TEMPLATE)
                    .evaluate(templateWriter, builder);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }
}
