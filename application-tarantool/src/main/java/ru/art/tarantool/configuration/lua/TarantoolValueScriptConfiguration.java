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

package ru.art.tarantool.configuration.lua;

import com.mitchellbosecke.pebble.*;
import com.mitchellbosecke.pebble.loader.*;
import lombok.*;
import ru.art.tarantool.exception.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.VALUE;
import java.io.*;
import java.util.*;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor(staticName = "tarantoolValueScript")
@RequiredArgsConstructor(staticName = "tarantoolValueScript")
public class TarantoolValueScriptConfiguration {
    private final String spaceName;
    private String indexName;

    public String toLua() {
        StringWriter templateWriter = new StringWriter();
        try {
            Map<String, Object> templateContext = cast(mapOf(SPACE_NAME, spaceName).toMap(INDEX_NAME, indexName));
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .build()
                    .getTemplate(VALUE + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

}
