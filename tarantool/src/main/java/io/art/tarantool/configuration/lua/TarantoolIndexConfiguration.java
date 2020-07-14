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
import static java.util.stream.Collectors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.core.constants.CharConstants.CLOSING_BRACES;
import static io.art.core.constants.CharConstants.EQUAL;
import static io.art.core.constants.CharConstants.OPENING_BRACES;
import static io.art.core.constants.CharConstants.SINGLE_QUOTE;
import static io.art.core.constants.StringConstants.COMMA;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolFieldType.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Templates.*;
import java.io.*;
import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor(staticName = "tarantoolIndex")
@SuppressWarnings("Duplicates")
public class TarantoolIndexConfiguration {
    private final String indexName;
    private final String spaceName;
    @Singular("part")
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
        Map<String, Object> templateContext = cast(mapOf()
                .add(INDEX_NAME, indexName)
                .add(SPACE_NAME, spaceName)
                .add(TYPE, type)
                .add(ID_FIELD, id)
                .add(UNIQUE, unique)
                .add(DISTANCE, distance)
                .add(DIMENSION, dimension)
                .add(BLOOM_FPR, bloomFpr)
                .add(PAGE_SIZE, pageSize)
                .add(RANGE_SIZE, rangeSize)
                .add(RUN_COUNT_PER_LEVEL, runCountPerLevel)
                .add(RUN_SIZE_RATIO, runSizeRatio)
                .add(SEQUENCE, sequence));
        if (isNotEmpty(parts)) {
            templateContext.put(PARTS, OPENING_BRACES + parts.stream().map(Part::toString).collect(joining(COMMA)) + CLOSING_BRACES);
        }
        StringWriter templateWriter = new StringWriter();
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(CREATE_INDEX + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

    public String toAlterIndexLua() {
        Map<String, Object> templateContext = cast(mapOf()
                .add(INDEX_NAME, indexName)
                .add(SPACE_NAME, spaceName)
                .add(TYPE, type)
                .add(ID_FIELD, id)
                .add(UNIQUE, unique)
                .add(DISTANCE, distance)
                .add(DIMENSION, dimension)
                .add(BLOOM_FPR, bloomFpr)
                .add(PAGE_SIZE, pageSize)
                .add(RANGE_SIZE, rangeSize)
                .add(RUN_COUNT_PER_LEVEL, runCountPerLevel)
                .add(RUN_SIZE_RATIO, runSizeRatio)
                .add(SEQUENCE, sequence));
        if (isNotEmpty(parts)) {
            templateContext.put(PARTS, OPENING_BRACES + parts.stream().map(Part::toString).collect(joining(COMMA)) + CLOSING_BRACES);
        }
        StringWriter templateWriter = new StringWriter();
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(ALTER_INDEX + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
    }

    public String toManageIndexLua() {
        Map<String, Object> templateContext = cast(mapOf()
                .add(INDEX_NAME, indexName)
                .add(SPACE_NAME, spaceName));
        StringWriter templateWriter = new StringWriter();
        try {
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(INDEX_MANAGEMENT + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            return templateWriter.toString();
        } catch (Throwable e) {
            throw new TarantoolExecutionException(e);
        }
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
