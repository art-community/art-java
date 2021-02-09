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

package io.art.rocks.db.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.rocks.db.configuration.*;
import io.art.rocks.db.state.*;
import lombok.*;
import org.rocksdb.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.wrapper.ExceptionWrapper.wrapExceptionCall;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static org.rocksdb.RocksDB.*;


@Getter
public class RocksDbModule implements StatefulModule<RocksDbModuleConfiguration, RocksDbModuleConfiguration.Configurator, RocksDbModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<RocksDbModuleConfiguration, RocksDbModuleState> rocksDbModule = context().getStatefulModule(RocksDbModule.class.getSimpleName());
    private final String id = RocksDbModule.class.getSimpleName();
    private final RocksDbModuleConfiguration configuration = new RocksDbModuleConfiguration();
    private final RocksDbModuleConfiguration.Configurator configurator = new RocksDbModuleConfiguration.Configurator(configuration);
    private RocksDbModuleState state;

    public static StatefulModuleProxy<RocksDbModuleConfiguration, RocksDbModuleState> rocksDbModule() {
        return getRocksDbModule();
    }

    @Override
    @SneakyThrows
    public void onLoad(Context.Service contextService) {
        loadLibrary();
        String path = configuration.getPath();
        createDirectories(get(path));
        state = new RocksDbModuleState(lazy(() -> wrapExceptionCall(() -> open(configuration.getOptions(), path))));
    }

    @Override
    public void onUnload(Context.Service contextService) {
        if (isNull(state)) return;
        RocksDB db = state.db();
        if (nonNull(db)) {
            db.close();
        }
    }
}
