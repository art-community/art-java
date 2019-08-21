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

import lombok.*;
import org.jtwig.*;
import java.util.*;

import static java.util.stream.Collectors.*;
import static org.jtwig.JtwigTemplate.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.CharConstants.EQUAL;
import static ru.art.core.constants.CharConstants.SINGLE_QUOTE;
import static ru.art.core.constants.StringConstants.CLOSING_BRACES;
import static ru.art.core.constants.StringConstants.COMMA;
import static ru.art.core.constants.StringConstants.OPENING_BRACES;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.USER;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.*;

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
        JtwigModel model = new JtwigModel()
                .with(SPACE_NAME, spaceName)
                .with(ID_FIELD, id)
                .with(TEMPORARY, temporary)
                .with(ENGINE, engine)
                .with(FIELD_COUNT, fieldCount)
                .with(IS_LOCAL, isLocal)
                .with(USER, user);
        if (!isEmpty(formats)) {
            model.with(FORMAT, OPENING_BRACES + formats.stream().map(Format::toString).collect(joining()) + CLOSING_BRACES);
        }
        return classpathTemplate(CREATE_SPACE + JTW_EXTENSION).render(model);
    }

    public String toFormatSpaceLua() {
        return classpathTemplate(FORMAT_SPACE + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(SPACE_NAME, spaceName)
                        .with(FORMAT, OPENING_BRACES + formats.stream().map(Format::toString).collect(joining()) + CLOSING_BRACES));
    }

    public String toManageSpaceLua() {
        return classpathTemplate(SPACE_MANAGEMENT + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(SPACE_NAME, spaceName));
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
