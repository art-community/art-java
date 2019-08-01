package ru.adk.config.remote.constants;

public interface RemoteConfigLoaderConstants {
    String MODULE_ID_IS_EMPTY = "Module id is empty";
    String CONFIGURATOR_CONNECTION_PROPERTIES_NOT_EXISTS = "Configurator host or port not exists in module-config. Using local file configuration mode";
    String CONFIGURATION_IS_EMPTY = "Remote configuration is empty or configurator was not launched. Using local file configuration mode";

    interface LocalConfigKeys {
        String CONFIGURATOR_HOST = "configurator.host";
        String CONFIGURATOR_PORT = "configurator.port";
    }
}
