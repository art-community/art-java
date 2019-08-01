package ru.art.config;

import groovy.util.ConfigObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.config.constants.ConfigType;
import ru.art.config.exception.ConfigException;
import ru.art.core.checker.CheckerForEmptiness;
import ru.art.entity.Entity;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static ru.art.config.constants.ConfigExceptionMessages.*;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            case HOCON: //Temporary return false, because we cannot determine emptiness of config
            case YAML:
                return false;
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


    public io.advantageous.config.Config asTypesafeConfig() {
        if (!configType.isTypesafeConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_TYPESAFE);
        return (io.advantageous.config.Config) this.configObject;
    }

    public io.advantageous.config.Config asYamlConfig() {
        if (!configType.isYamlConfig()) throw new ConfigException(CONFIG_TYPE_IS_NOT_YAML);
        return (io.advantageous.config.Config) this.configObject;
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
                return new Config(asYamlConfig().getConfig(sectionId), configType);
            case GROOVY:
                return new Config(asGroovyConfig().get(sectionId), configType);
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
                return asYamlConfig().getString(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getInt(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getLong(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getDouble(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getBoolean(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }


    @SuppressWarnings("unchecked")
    public List<Config> getConfigList(String path) {
        if (isEmpty(this)) return emptyList();
        if (CheckerForEmptiness.isEmpty(path)) throw new ConfigException(PATH_IS_EMPTY);
        if (!hasPath(path)) return emptyList();
        switch (configType) {
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findEntityList(path).stream().map(configObject -> new Config(configObject, configType)).collect(toList());
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getConfigList(path).stream().map(configObject -> new Config(configObject, configType)).collect(toList());
            case YAML:
                return asYamlConfig().getConfigList(path).stream().map(configObject -> new Config(configObject, configType)).collect(toList());
            case GROOVY:
                return ((Map<String, ?>) asGroovyConfig().get(path)).values().stream().map(configObject -> new Config(configObject, configType)).collect(toList());
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
                return asYamlConfig().getStringList(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getIntList(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getDoubleList(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getLongList(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
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
                return asYamlConfig().getBooleanList(path);
            case GROOVY:
                return cast(asGroovyConfig().get(path));
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }


    @SuppressWarnings("unchecked")
    public Set<String> getKeys(String path) {
        switch (configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return asTypesafeConfig().getMap(path).keySet();
            case YAML:
                return asYamlConfig().getMap(path).keySet();
            case GROOVY:
                return ((Map<String, ?>) asGroovyConfig().get(path)).keySet();
            case REMOTE_ENTITY_CONFIG:
                return asEntityConfig().findEntity(path).getFieldNames();
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
                return asYamlConfig().hasPath(path);
            case GROOVY:
                return asGroovyConfig().containsKey(path);
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }
}
