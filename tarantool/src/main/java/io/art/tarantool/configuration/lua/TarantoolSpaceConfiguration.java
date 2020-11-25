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
import lombok.*;
import io.art.tarantool.exception.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.CharacterConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static java.util.stream.Collectors.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.USER;
import static io.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Templates.*;
import java.io.*;
import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "tarantoolSpace")
@SuppressWarnings("Duplicates")
public class TarantoolSpaceConfiguration {
    private final String spaceName;
    @Singular("format")
    private Set<Format> formats;
    private Integer id;
    private Boolean temporary;
    private String engine;
    private Integer fieldCount;
    private Boolean isLocal;
    private String user;

    public String toCreateSpaceLua() {
        Map<String, Object> templateContext = cast(mapOf()
                .add(SPACE_NAME, spaceName)
                .add(ID_FIELD, id)
                .add(TEMPORARY, temporary)
                .add(ENGINE, engine)
                .add(FIELD_COUNT, fieldCount)
                .add(IS_LOCAL, isLocal)
                .add(USER, user));
        if (!isEmpty(formats)) {
            templateContext.put(FORMAT, OPENING_BRACES + formats.stream().map(Format::toString).collect(joining()) + CLOSING_BRACES);
        }
        StringWriter templateWriter = new StringWriter();
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(CREATE_SPACE + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

    public String toFormatSpaceLua() {
        StringWriter templateWriter = new StringWriter();
        Map<String, Object> templateContext = cast(mapOf()
                .add(SPACE_NAME, spaceName)
                .add(FORMAT, OPENING_BRACES + formats.stream().map(Format::toString).collect(joining()) + CLOSING_BRACES));
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(FORMAT_SPACE + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

    public String toManageSpaceLua() {
        StringWriter templateWriter = new StringWriter();
        Map<String, Object> templateContext = cast(mapOf().add(SPACE_NAME, spaceName));
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(SPACE_MANAGEMENT + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

    @Getter
    @Builder
    public static class Format {
        private final String fieldName;
        @Builder.Default
        private final TarantoolFieldType type = UNSIGNED;
        @Builder.Default
        private final boolean isNullable = false;

        @Override
        public String toString() {
            String part = OPENING_BRACES + SINGLE_QUOTE + fieldName + SINGLE_QUOTE + COMMA + SINGLE_QUOTE + type.name().toLowerCase() + SINGLE_QUOTE;
            if (isNullable) {
                return part + COMMA + IS_NULLABLE + EQUAL + true + CLOSING_BRACES;
            }
            return part + CLOSING_BRACES;
        }
    }
}
