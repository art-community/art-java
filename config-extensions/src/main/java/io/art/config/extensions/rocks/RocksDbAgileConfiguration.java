/*
 * ART Java
 *
 * Copyright 2019 ART
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

package io.art.config.extensions.rocks;

import lombok.*;
import io.art.rocks.db.configuration.RocksDbModuleConfiguration.*;
import static io.art.config.extensions.ConfigExtensions.*;
import static io.art.config.extensions.common.CommonConfigKeys.*;
import static io.art.config.extensions.rocks.RocksDbConfigKeys.*;

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
