/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.configuration.yaml.source;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.dataformat.yaml.*;
import io.art.configuration.yaml.exception.*;
import io.art.core.factory.*;
import io.art.core.source.*;
import lombok.*;
import static com.fasterxml.jackson.databind.node.JsonNodeType.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.combiner.SectionCombiner.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.parser.DurationParser.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;
import java.io.*;
import java.time.*;
import java.util.*;

@Getter
public class YamlConfigurationSource implements ConfigurationSource {
    private final String section;
    private final ModuleConfigurationSourceType type;
    private final File file;
    private final JsonNode configuration;
    private static final YAMLMapper yamlMapper = new YAMLMapper();

    public YamlConfigurationSource(String section, ModuleConfigurationSourceType type, File file) {
        this.section = section;
        this.type = type;
        this.file = file;
        try {
            configuration = yamlMapper.readTree(file);
        } catch (IOException exception) {
            throw new YamlConfigurationLoadingException(exception);
        }
    }

    public YamlConfigurationSource(String section, ModuleConfigurationSourceType type, File file, JsonNode configuration) {
        this.section = section;
        this.type = type;
        this.file = file;
        this.configuration = configuration;
    }

    @Override
    public Integer getInt(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asInt);
    }

    @Override
    public Long getLong(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asLong);
    }

    @Override
    public Boolean getBool(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asBoolean);
    }

    @Override
    public Double getDouble(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asDouble);
    }

    @Override
    public Float getFloat(String path) {
        return let(getDouble(path), Number::floatValue);
    }

    @Override
    public String getString(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), JsonNode::asText);
    }

    @Override
    public Duration getDuration(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), node -> parseDuration(node.asText()));
    }

    @Override
    public ConfigurationSource getNested(String path) {
        return orNull(getYamlConfigNode(path), node -> !node.isMissingNode(), node -> new YamlConfigurationSource(combine(section, path), type, file, node));
    }

    @Override
    public List<Integer> getIntList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asInt)
                .collect(toCollection(ArrayFactory::dynamicArray));
    }

    @Override
    public List<Long> getLongList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asLong)
                .collect(toCollection(ArrayFactory::dynamicArray));
    }

    @Override
    public List<Boolean> getBoolList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asBoolean)
                .collect(toCollection(ArrayFactory::dynamicArray));
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asDouble)
                .collect(toCollection(ArrayFactory::dynamicArray));

    }

    @Override
    public List<String> getStringList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(JsonNode::asText)
                .collect(toCollection(ArrayFactory::dynamicArray));
    }

    @Override
    public List<Duration> getDurationList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(node -> parseDuration(node.asText()))
                .collect(toCollection(ArrayFactory::dynamicArray));

    }

    @Override
    public List<ConfigurationSource> getNestedList(String path) {
        return stream(((Iterable<JsonNode>) () -> getYamlConfigNode(path).iterator()).spliterator(), false)
                .map(node -> new YamlConfigurationSource(combine(section, path), type, file, node))
                .collect(toCollection(ArrayFactory::dynamicArray));
    }

    @Override
    @SuppressWarnings(NULLABLE_PROBLEMS)
    public Set<String> getKeys() {
        return stream(((Iterable<String>) configuration::fieldNames).spliterator(), false).collect(toCollection(SetFactory::set));
    }

    @Override
    public boolean has(String path) {
        JsonNodeType nodeType = getYamlConfigNode(path).getNodeType();
        return nodeType != NULL && nodeType != MISSING;
    }

    private JsonNode getYamlConfigNode(String path) {
        JsonNode yamlConfig = configuration;
        JsonNode node = yamlConfig.path(path);
        JsonNodeType nodeType = node.getNodeType();
        if (nodeType != NULL && nodeType != MISSING) {
            return node;
        }
        int dotIndex = path.indexOf(DOT);
        if (dotIndex == -1) {
            return MissingNode.getInstance();
        }
        node = yamlConfig.path(path.substring(0, dotIndex));
        path = path.substring(dotIndex + 1);
        while (true) {
            JsonNode valueNode = node.path(path);
            JsonNodeType valueNodeType = valueNode.getNodeType();
            switch (valueNodeType) {
                case OBJECT:
                case BINARY:
                case BOOLEAN:
                case NUMBER:
                case ARRAY:
                case STRING:
                    return valueNode;
                case MISSING:
                case POJO:
                case NULL:
                    break;
            }
            dotIndex = path.indexOf(DOT);
            if (dotIndex == -1) {
                return MissingNode.getInstance();
            }
            node = node.path(path.substring(0, dotIndex));
            path = path.substring(dotIndex + 1);
        }
    }
}
