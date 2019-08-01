package ru.adk.config.constants;

public interface ConfigExceptionMessages {
    String SECTION_ID_IS_NULL = "SectionId is null";
    String CONFIG_ID_IS_NULL = "ConfigId is null";
    String SECTION_ID_IS_EMPTY = "SectionId is empty";
    String PATH_IS_EMPTY = "Path is empty";
    String CONFIG_TYPE_IS_NULL = "ConfigType is null";
    String UNKNOWN_CONFIG_TYPE = "Unknown config type: ''{0}'' ";
    String CONFIG_TYPE_IS_NOT_TYPESAFE = "Config type is not 'typesafe'";
    String CONFIG_TYPE_IS_NOT_YAML = "Config type is not 'yaml'";
    String CONFIG_TYPE_IS_NOT_GROOVY = "Config type is not 'groovy'";
    String CONFIG_TYPE_IS_NOT_ENTITY = "Config type is not 'entity'";
}
