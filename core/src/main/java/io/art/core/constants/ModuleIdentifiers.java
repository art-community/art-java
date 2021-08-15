package io.art.core.constants;

import io.art.core.collection.*;

public interface ModuleIdentifiers {
    String LOGGING_MODULE_ID = "LoggingModule";
    String TRANSPORT_MODULE_ID = "TransportModule";
    String JSON_MODULE_ID = "JsonModule";
    String YAML_MODULE_ID = "YamlModule";
    String CONFIGURATOR_MODULE_ID = "ConfiguratorModule";
    String META_MODULE_ID = "MetaModule";

    ImmutableSet<String> PRELOADED_MODULES = ImmutableSet.<String>immutableSetBuilder()
            .add(CONFIGURATOR_MODULE_ID)
            .add(LOGGING_MODULE_ID)
            .add(TRANSPORT_MODULE_ID)
            .build();

    ImmutableSet<String> POST_LOADED_MODULES = ImmutableSet.<String>immutableSetBuilder()
            .add(META_MODULE_ID)
            .build();
}
