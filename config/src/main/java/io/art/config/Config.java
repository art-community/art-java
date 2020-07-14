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

package io.art.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import lombok.*;
import io.art.config.constants.*;
import io.art.config.exception.*;
import io.art.core.checker.*;
import io.art.entity.*;
import io.art.entity.Value;
import static com.fasterxml.jackson.databind.node.JsonNodeType.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.Spliterator.*;
import static java.util.Spliterators.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;
import static io.art.config.constants.ConfigExceptionMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.entity.Value.isPrimitive;
import java.util.*;
import java.util.stream.*;

@Getter
@AllArgsConstructor
public class Config {
    private final Object configObject;
    private final ConfigType configType;

    public static boolean isEmpty(Config config) {
        if (isNull(config)) return true;
        if (isNull(config.configObject) || isNull(config.configType)) return true;
        switch (config.configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return config.asTypesafeConfig().isEmpty();
            case YAML:
                JsonNode node = config.asYamlConfig();
                return node.size() <= 0 || node.getNodeType() == NULL || node.getNodeType() == MISSING;
            case REMOTE_ENTITY_CONFIG:
                return config.asEntityConfig().isEmpty();
            default:
                return false;
        }
    }

    public static boolean isNotEmpty(Config config) {
        return !Config.isEmpty(config);
    }


    public com.typesafe.config.Config asTypesafeConfig() {
        if (!configType.isTypesafeConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_TYPESAFE);
        return ((com.typesafe.config.ConfigObject) this.configObject).toConfig();
    }

    public JsonNode asYamlConfig() {
        if (!configType.isYamlConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_YAML);
        return (JsonNode) this.configObject;
    }

    public Entity asEntityConfig() {
        if (!configType.isRemoteEntityConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_ENTITY);
        return (Entity) this.configObject;
    }


    public Config getConfig(String sectionId) {
        if (isNull(sectionId)) throw new ConfigException(SECTION_ID_IS_NULL);
        if (!hasPath(sectionId)) return null;
        switch (configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return new Config(asTypesafeConfig().getConfig(sectionId), configType);
            case YAML:
                return new Config(asYamlConfig().at(SLASH + sectionId.replace(DOT, SLASH)), configType);
            case REMOTE_ENTITY_CONFIG:
                return new Config(asEntityConfig().find(sectionId), configType);
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));

        }
    }

    public String getString(String path) {
        if (isEmpty(this)) return EMPTY_STRING;
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return EMPTY_STRING;
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findString(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getString(path);
            case YAML:
                return asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).asText();
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Integer getInt(String path) {
        if (isEmpty(this)) return null;
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return null;
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findInt(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getInt(path);
            case YAML:
                return asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).asInt();
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Long getLong(String path) {
        if (isEmpty(this)) return null;
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return null;
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findLong(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getLong(path);
            case YAML:
                return asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).asLong();
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Double getDouble(String path) {
        if (isEmpty(this)) return null;
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return null;
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findDouble(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getDouble(path);
            case YAML:
                return asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).asDouble();
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Boolean getBool(String path) {
        if (isEmpty(this)) return null;
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return null;
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findBool(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getBoolean(path);
            case YAML:
                return asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).asBoolean();
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }


    public List<Config> getConfigList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        Map<String, ?> config;
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findEntityList(path).stream().map(configObject -> new Config(configObject, configType)).collect(toList());
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getConfigList(path).stream().map(configObject -> new Config(configObject, configType)).collect(toList());
            case YAML:
                return stream(spliteratorUnknownSize(asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).iterator(), ORDERED), false)
                        .map(configObject -> new Config(configObject, configType))
                        .collect(toList());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public List<String> getStringList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findStringList(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getStringList(path);
            case YAML:
                return stream(((Iterable<JsonNode>) () -> asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).iterator()).spliterator(), false)
                        .map(JsonNode::asText)
                        .collect(toList());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public List<Integer> getIntList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findIntList(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getIntList(path);
            case YAML:
                return stream(((Iterable<JsonNode>) () -> asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).iterator()).spliterator(), false)
                        .map(JsonNode::asInt)
                        .collect(toList());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public List<Double> getDoubleList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findDoubleList(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getDoubleList(path);
            case YAML:
                return stream(((Iterable<JsonNode>) () -> asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).iterator()).spliterator(), false)
                        .map(JsonNode::asDouble)
                        .collect(toList());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public List<Long> getLongList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findLongList(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getLongList(path);
            case YAML:
                return stream(((Iterable<JsonNode>) () -> asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).iterator()).spliterator(), false)
                        .map(JsonNode::asLong)
                        .collect(toList());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public List<Boolean> getBoolList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findBoolList(path);
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getBooleanList(path);
            case YAML:
                return stream(((Iterable<JsonNode>) () -> asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).iterator()).spliterator(), false)
                        .map(JsonNode::asBoolean)
                        .collect(toList());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }


    public Set<String> getKeys(String path) {
        switch (configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getObject(path).keySet();
            case YAML:
                return stream(((Iterable<String>) () -> asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).fieldNames()).spliterator(), false).collect(toSet());
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findEntity(path).getFieldKeys().stream().filter(Value::isPrimitive).map(Object::toString).collect(toSet());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));

        }
    }

    public Set<String> getKeys() {
        switch (configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().root().keySet();
            case YAML:
                return stream(((Iterable<String>) () -> asYamlConfig().fieldNames()).spliterator(), false).collect(toSet());
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().getFieldKeys().stream().filter(Value::isPrimitive).map(Object::toString).collect(toSet());
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));

        }
    }


    public boolean hasPath(String path) {
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return nonNull(asEntityConfig().find(path));
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().hasPath(path);
            case YAML:
                JsonNodeType nodeType = asYamlConfig().at(SLASH + path.replace(DOT, SLASH)).getNodeType();
                return nodeType != NULL && nodeType != MISSING;
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Properties getProperties(String path) {
        if (!hasPath(path)) {
            return new Properties();
        }
        Properties properties = new Properties();
        properties.putAll(getKeys(path).stream().collect(toMap(identity(), key -> getString(path + DOT + key))));
        return properties;
    }
}
