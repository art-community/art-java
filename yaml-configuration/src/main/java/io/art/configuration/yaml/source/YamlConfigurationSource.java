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
import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static com.fasterxml.jackson.databind.node.JsonNodeType.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.combiner.SectionCombiner.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.Objects.*;
import static java.util.Spliterator.*;
import static java.util.Spliterators.*;
import static java.util.stream.StreamSupport.*;
import java.io.*;
import java.util.function.*;

@Getter
public class YamlConfigurationSource implements NestedConfiguration {
    private final String section;
    private final ModuleConfigurationSourceType type;
    private final File file;
    private final JsonNode configuration;
    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();

    public YamlConfigurationSource(String section, ModuleConfigurationSourceType type, File file) {
        this.section = section;
        this.type = type;
        this.file = file;
        try {
            configuration = YAML_MAPPER.readTree(file);
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
    public Boolean asBool() {
        return let(configuration, JsonNode::asBoolean);
    }

    @Override
    public String asString() {
        return let(configuration, JsonNode::asText);
    }

    @Override
    public ImmutableArray<NestedConfiguration> asArray() {
        if (isNull(configuration)) return emptyImmutableArray();
        if (configuration.getNodeType() != ARRAY) {
            return emptyImmutableArray();
        }
        return stream(spliterator(configuration.elements(), configuration.size(), IMMUTABLE), false)
                .filter(YamlConfigurationSource::isValid)
                .map(node -> new YamlConfigurationSource(section, type, file, node))
                .collect(immutableArrayCollector());
    }

    @Override
    public <T> ImmutableArray<T> asArray(Function<NestedConfiguration, T> mapper) {
        if (isNull(configuration)) return emptyImmutableArray();
        if (configuration.getNodeType() != ARRAY) {
            return emptyImmutableArray();
        }
        return stream(spliterator(configuration.elements(), configuration.size(), IMMUTABLE), false)
                .filter(YamlConfigurationSource::isValid)
                .map(node -> mapper.apply(new YamlConfigurationSource(section, type, file, node)))
                .collect(immutableArrayCollector());
    }

    @Override
    public NestedConfiguration getNested(String path) {
        JsonNode configNode = getYamlConfigNode(path);
        return orNull(configNode, YamlConfigurationSource::isValid, node -> new YamlConfigurationSource(combine(section, path), type, file, node));
    }

    @Override
    @SuppressWarnings(NULLABLE_PROBLEMS)
    public ImmutableSet<String> getKeys() {
        return stream(((Iterable<String>) configuration::fieldNames).spliterator(), false).collect(immutableSetCollector());
    }

    @Override
    public boolean has(String path) {
        JsonNode nodeType = getYamlConfigNode(path);
        return nodeType.isNull() && !nodeType.isMissingNode();
    }

    private JsonNode getYamlConfigNode(String path) {
        JsonNode yamlConfig = configuration;
        JsonNode node = yamlConfig.path(path);
        if (isValid(node)) return node;
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

    private static boolean isValid(JsonNode node) {
        return !node.isNull() && !node.isMissingNode();
    }

}
