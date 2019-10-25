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

package ru.art.config;

import groovy.util.*;
import lombok.*;
import ru.art.config.constants.*;
import ru.art.config.exception.*;
import ru.art.core.checker.*;
import ru.art.core.finder.*;
import ru.art.entity.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.config.constants.ConfigExceptionMessages.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.core.finder.MapEntryFinder.*;
import java.util.*;

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
                return config.asYamlConfig().isEmpty();
            case GROOVY:
                return config.asGroovyConfig().isEmpty();
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

    public Map<String, Object> asYamlConfig() {
        if (!configType.isYamlConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_YAML);
        return cast(this.configObject);
    }

    public ConfigObject asGroovyConfig() {
        if (!configType.isGroovyConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_GROOVY);
        return (ConfigObject) this.configObject;
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
                return new Config(find(asYamlConfig(), sectionId), configType);
            case GROOVY:
                return new Config(find(cast(asGroovyConfig()), sectionId), configType);
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
                return cast(find(cast(asYamlConfig()), path));
            case GROOVY:
                return cast(find(cast(asGroovyConfig()), path));
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
                return cast(find(cast(asYamlConfig()), path));
            case GROOVY:
                return cast(find(cast(asGroovyConfig()), path));
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
                return cast(find(cast(asYamlConfig()), path));
            case GROOVY:
                return cast(find(cast(asGroovyConfig()), path));
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
                return cast(find(cast(asYamlConfig()), path));
            case GROOVY:
                return cast(find(cast(asGroovyConfig()), path));
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
                return cast(find(cast(asYamlConfig()), path));
            case GROOVY:
                return cast(find(cast(asGroovyConfig()), path));
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
                return isNull(config = find(cast(asYamlConfig()), path))
                        ? emptyList()
                        : config.values().stream().map(configObject -> new Config(configObject, configType)).collect(toList());
            case GROOVY:
                return isNull(config = find(cast(asGroovyConfig()), path))
                        ? emptyList()
                        : config.values().stream().map(configObject -> new Config(configObject, configType)).collect(toList());
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
                return cast(fixedArrayOf((Collection<?>) find(cast(asYamlConfig()), path)));
            case GROOVY:
                return cast(fixedArrayOf((Collection<?>) find(cast(asGroovyConfig()), path)));
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
                return fixedArrayOf((Collection<?>) find(cast(asYamlConfig()), path)).stream().map(element -> (Number) element).map(Number::intValue).collect(toList());
            case GROOVY:
                return fixedArrayOf((Collection<?>) find(cast(asGroovyConfig()), path)).stream().map(element -> (Number) element).map(Number::intValue).collect(toList());
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
                return fixedArrayOf((Collection<?>) find(cast(asYamlConfig()), path)).stream().map(element -> (Number) element).map(Number::doubleValue).collect(toList());
            case GROOVY:
                return fixedArrayOf((Collection<?>) find(cast(asGroovyConfig()), path)).stream().map(element -> (Number) element).map(Number::doubleValue).collect(toList());
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
                return fixedArrayOf((Collection<?>) find(cast(asYamlConfig()), path)).stream().map(element -> (Number) element).map(Number::longValue).collect(toList());
            case GROOVY:
                return fixedArrayOf((Collection<?>) find(cast(asGroovyConfig()), path)).stream().map(element -> (Number) element).map(Number::longValue).collect(toList());
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
                return cast(fixedArrayOf((Collection<?>) find(cast(asYamlConfig()), path)));
            case GROOVY:
                return cast(fixedArrayOf((Collection<?>) find(cast(asGroovyConfig()), path)));
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Set<String> getKeys(String path) {
        Map<String, ?> config;
        switch (configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getObject(path).keySet();
            case YAML:
                return isNull(config = find(cast(asYamlConfig()), path)) ? emptySet() : config.keySet();
            case GROOVY:
                return isNull(config = find(cast(asGroovyConfig()), path)) ? emptySet() : config.keySet();
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findEntity(path).getFieldNames();
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
                return cast(asYamlConfig().keySet());
            case GROOVY:
                return cast(asGroovyConfig().keySet());
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().getFieldNames();
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
                return MapEntryFinder.hasPath(cast(asYamlConfig()), path);
            case GROOVY:
                return MapEntryFinder.hasPath(cast(asGroovyConfig()), path);
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    public Properties getProperties(String path) {
        if (!hasPath(path)) {
            return new Properties();
        }
        Properties additionalProperties = new Properties();
        additionalProperties.putAll(getKeys(path).stream()
                .collect(toMap(identity(), this::getString)));
        return additionalProperties;
    }
}
