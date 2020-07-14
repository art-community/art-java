/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rocks.db.configuration;

import lombok.*;
import org.rocksdb.*;
import io.art.core.module.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.DefaultOptions.*;
import java.io.*;

public interface RocksDbModuleConfiguration extends ModuleConfiguration {
    String getPath();

    Options getOptions();

    boolean isEnableTracing();

    RocksDbModuleDefaultConfiguration DEFAULT_CONFIGURATION = new RocksDbModuleDefaultConfiguration();

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
