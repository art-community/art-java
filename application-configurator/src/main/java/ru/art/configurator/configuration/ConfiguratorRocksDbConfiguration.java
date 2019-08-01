package ru.art.configurator.configuration;

import lombok.Getter;
import ru.art.rocks.db.configuration.RocksDbModuleConfiguration.RocksDbModuleDefaultConfiguration;
import static ru.art.config.ConfigProvider.config;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_ROCKS_DB_PATH;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.CONFIGURATOR_SECTION_ID;

@Getter
public class ConfiguratorRocksDbConfiguration extends RocksDbModuleDefaultConfiguration {
    private boolean enableTracing = false;
    private String path = config(CONFIGURATOR_SECTION_ID).asYamlConfig().getString(CONFIGURATOR_ROCKS_DB_PATH);
}
