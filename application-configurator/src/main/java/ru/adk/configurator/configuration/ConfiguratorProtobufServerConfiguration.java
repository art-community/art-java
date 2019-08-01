package ru.adk.configurator.configuration;

import lombok.Getter;
import ru.adk.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerModuleDefaultConfiguration;
import static ru.adk.config.ConfigProvider.config;
import static ru.adk.config.constants.ConfigType.YAML;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_PATH;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_GRPC_PORT_PROPERTY;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_SECTION_ID;

@Getter
public class ConfiguratorProtobufServerConfiguration extends GrpcServerModuleDefaultConfiguration {
    private final String path = CONFIGURATOR_PATH;
    private final int port = config(CONFIGURATOR_SECTION_ID, YAML).getInt(CONFIGURATOR_GRPC_PORT_PROPERTY);
}
