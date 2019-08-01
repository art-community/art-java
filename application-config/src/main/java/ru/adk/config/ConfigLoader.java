package ru.adk.config;

import com.typesafe.config.ConfigSyntax;
import lombok.NoArgsConstructor;
import ru.adk.config.constants.ConfigType;
import ru.adk.config.exception.ConfigException;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.config.GroovyConfigLoader.loadGroovyConfig;
import static ru.adk.config.TypesafeConfigLoader.loadTypeSafeConfig;
import static ru.adk.config.YamlConfigLoader.loadYamlConfig;
import static ru.adk.config.constants.ConfigExceptionMessages.CONFIG_TYPE_IS_NOT_TYPESAFE;
import static ru.adk.config.constants.ConfigExceptionMessages.UNKNOWN_CONFIG_TYPE;

@NoArgsConstructor(access = PRIVATE)
class ConfigLoader {
    static Config loadLocalConfig(String configId, ConfigType configType) {
        switch (configType) {
            case PROPERTIES:
            case JSON:
            case HOCON:
                return new Config(loadTypeSafeConfig(configId, toTypesafeConfigSyntax(configType)), configType);
            case GROOVY:
                return new Config(loadGroovyConfig(configId), configType);
            case YAML:
                return new Config(loadYamlConfig(configId), configType);
            default:
                throw new ConfigException(format(UNKNOWN_CONFIG_TYPE, configType));
        }
    }

    private static ConfigSyntax toTypesafeConfigSyntax(ConfigType configType) {
        switch (configType) {
            case PROPERTIES:
                return ConfigSyntax.PROPERTIES;
            case JSON:
                return ConfigSyntax.JSON;
            case HOCON:
                return ConfigSyntax.CONF;
            default:
                throw new ConfigException(CONFIG_TYPE_IS_NOT_TYPESAFE);
        }
    }
}