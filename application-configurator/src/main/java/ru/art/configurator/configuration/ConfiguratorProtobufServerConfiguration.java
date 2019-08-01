package ru.art.configurator.configuration;

import lombok.Getter;
import ru.art.grpc.server.configuration.GrpcServerModuleConfiguration.GrpcServerModuleDefaultConfiguration;
import static ru.art.config.ConfigProvider.config;
import static ru.art.config.constants.ConfigType.YAML;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.CONFIGURATOR_PATH;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_GRPC_PORT_PROPERTY;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_SECTION_ID;

@Getter
public class ConfiguratorProtobufServerConfiguration extends GrpcServerModuleDefaultConfiguration {
    private final String path = CONFIGURATOR_PATH;
    private final int port = config(CONFIGURATOR_SECTION_ID, YAML).getInt(CONFIGURATOR_GRPC_PORT_PROPERTY);
}
