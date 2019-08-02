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

import lombok.*;
import org.jtwig.JtwigModel;
import static java.util.stream.Collectors.joining;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.CharConstants.*;
import static ru.art.core.constants.StringConstants.COMMA;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.STRING;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.UNSIGNED;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.*;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "tarantoolIndex")
@SuppressWarnings("Duplicates")
public class TarantoolIndexConfiguration {
    private final String indexName;
    private final String spaceName;
    @Singular
    private Set<Part> parts;
    private TarantoolIndexType type;
    private Integer id;
    @Builder.Default
    private boolean unique = true;
    private Integer dimension;
    private String distance;
    private Integer bloomFpr;
    private Integer pageSize;
    private Integer rangeSize;
    private Integer runCountPerLevel;
    private Integer runSizeRatio;
    private String sequence;

    public String toCreateIndexLua() {
        JtwigModel model = new JtwigModel()
                .with(INDEX_NAME, indexName)
                .with(SPACE_NAME, spaceName)
                .with(TYPE, type)
                .with(ID, id)
                .with(UNIQUE, unique)
                .with(DISTANCE, distance)
                .with(DIMENSION, dimension)
                .with(BLOOM_FPR, bloomFpr)
                .with(PAGE_SIZE, pageSize)
                .with(RANGE_SIZE, rangeSize)
                .with(RUN_COUNT_PER_LEVEL, runCountPerLevel)
                .with(RUN_SIZE_RATIO, runSizeRatio)
                .with(SEQUENCE, sequence);
        if (!isEmpty(parts)) {
            model.with(PARTS, OPENING_BRACES + parts.stream().map(Part::toString).collect(joining()) + CLOSING_BRACES);
        }
        return classpathTemplate(CREATE_INDEX + JTW_EXTENSION).render(model);
    }

    public String toAlterIndexLua() {
        JtwigModel model = new JtwigModel()
                .with(INDEX_NAME, indexName)
                .with(SPACE_NAME, spaceName)
                .with(TYPE, type)
                .with(ID, id)
                .with(UNIQUE, unique)
                .with(DISTANCE, distance)
                .with(DIMENSION, dimension)
                .with(BLOOM_FPR, bloomFpr)
                .with(PAGE_SIZE, pageSize)
                .with(RANGE_SIZE, rangeSize)
                .with(RUN_COUNT_PER_LEVEL, runCountPerLevel)
                .with(RUN_SIZE_RATIO, runSizeRatio)
                .with(SEQUENCE, sequence);
        if (!isEmpty(parts)) {
            model.with(PARTS, OPENING_BRACES + parts.stream().map(Part::toString).collect(joining()) + CLOSING_BRACES);
        }
        return classpathTemplate(ALTER_INDEX + JTW_EXTENSION).render(model);
    }

    public String toManageIndexLua() {
        return classpathTemplate(INDEX_MANAGEMENT + JTW_EXTENSION)
                .render(new JtwigModel()
                        .with(INDEX_NAME, indexName)
                        .with(SPACE_NAME, spaceName));
    }


    @Getter
    @Builder
    public static class Part {
        private final int fieldNumber;
        private String collation;
        @Builder.Default
        private final TarantoolFieldType type = UNSIGNED;
        @Builder.Default
        private final boolean isNullable = false;

        @Override
        public String toString() {
            String part = fieldNumber + COMMA + SINGLE_QUOTE + type.name().toLowerCase() + SINGLE_QUOTE;
            if (type == STRING && isNotEmpty(collation)) {
                part += COMMA + COLLATION + EQUAL + SINGLE_QUOTE + collation + SINGLE_QUOTE;
            }
            if (isNullable) {
                return part + COMMA + IS_NULLABLE + EQUAL + true;
            }
            return part;
        }
    }
}
