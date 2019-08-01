package ru.adk.config.extensions.rocks;

import lombok.Getter;
import ru.adk.rocks.db.configuration.RocksDbModuleConfiguration.RocksDbModuleDefaultConfiguration;
import static ru.adk.config.extensions.ConfigExtensions.configBoolean;
import static ru.adk.config.extensions.ConfigExtensions.configString;
import static ru.adk.config.extensions.common.CommonConfigKeys.ENABLE_TRACING;
import static ru.adk.config.extensions.common.CommonConfigKeys.PATH;
import static ru.adk.config.extensions.rocks.RocksDbConfigKeys.ROCKS_DB_SECTION_ID;

@Getter
public class RocksDbAgileConfiguration extends RocksDbModuleDefaultConfiguration {
    private boolean enableTracing;
    private String path;

    public RocksDbAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableTracing = configBoolean(ROCKS_DB_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        path = configString(ROCKS_DB_SECTION_ID, PATH, super.getPath());
    }
}
