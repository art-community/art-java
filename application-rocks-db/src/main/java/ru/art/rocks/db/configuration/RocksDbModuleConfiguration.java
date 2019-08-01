package ru.art.rocks.db.configuration;

import lombok.Getter;
import org.rocksdb.Options;
import ru.art.core.module.ModuleConfiguration;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.DEFAULT_PATH_TO_DB;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.DefaultOptions.DEFAULT_KEY_PREFIX_BYTES;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.DefaultOptions.DEFAULT_MERGE_OPERATOR;
import java.io.File;

public interface RocksDbModuleConfiguration extends ModuleConfiguration {
    String getPath();

    Options getOptions();

    boolean isEnableTracing();

    @Getter
    class RocksDbModuleDefaultConfiguration implements RocksDbModuleConfiguration {
        private final String path = new File(DEFAULT_PATH_TO_DB).getAbsolutePath();
        private final boolean enableTracing = false;
        private final Options options = new Options()
                .setMergeOperatorName(DEFAULT_MERGE_OPERATOR)
                .useCappedPrefixExtractor(DEFAULT_KEY_PREFIX_BYTES)
                .optimizeUniversalStyleCompaction()
                .setCreateIfMissing(true)
                .setAllowConcurrentMemtableWrite(false)
                .setMaxOpenFiles(-1)
                .setCreateMissingColumnFamilies(true);
    }
}
