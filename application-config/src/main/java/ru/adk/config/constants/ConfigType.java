package ru.adk.config.constants;

public enum ConfigType {
    PROPERTIES,
    JSON,
    HOCON,
    GROOVY,
    REMOTE_ENTITY_CONFIG,
    YAML;

    public boolean isTypesafeConfig() {
        return this == PROPERTIES || this == JSON || this == HOCON;
    }

    public boolean isGroovyConfig() {
        return this == GROOVY;
    }

    public boolean isYamlConfig() {
        return this == YAML;
    }

    public boolean isRemoteEntityConfig() {
        return this == REMOTE_ENTITY_CONFIG;
    }
}


