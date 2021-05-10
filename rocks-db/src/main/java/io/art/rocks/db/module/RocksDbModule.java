/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import io.art.core.operator.*;
import io.art.rocks.db.configuration.*;
import io.art.rocks.db.state.*;
import lombok.*;
import org.rocksdb.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.context.Context.*;
import static io.art.core.operator.Operators.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
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

    static {
        registerDefault(RocksDbModule.class.getSimpleName(), RocksDbModule::new);
    }

    public static StatefulModuleProxy<RocksDbModuleConfiguration, RocksDbModuleState> rocksDbModule() {
        return getRocksDbModule();
    }

    @Override
    public void onLoad(Context.Service contextService) {
        state = new RocksDbModuleState(lazy(() -> wrapExceptionCall(() -> {
            loadLibrary();
            createDirectories(get(configuration.getPath()));
            return open(configuration.getOptions(), configuration.getPath());
        })));
    }

    @Override
    public void onUnload(Context.Service contextService) {
        applyIf(state, RocksDbModuleState::initialized, state -> apply(state.db(), RocksDB::close));
    }
}
