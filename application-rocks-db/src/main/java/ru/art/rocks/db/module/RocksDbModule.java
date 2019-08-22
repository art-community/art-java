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

package ru.art.rocks.db.module;

import lombok.*;
import org.rocksdb.*;
import ru.art.core.module.*;
import ru.art.rocks.db.configuration.*;
import ru.art.rocks.db.configuration.RocksDbModuleConfiguration.*;
import ru.art.rocks.db.exception.*;
import ru.art.rocks.db.state.*;

import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static org.rocksdb.RocksDB.*;
import static ru.art.core.context.Context.*;
import static ru.art.rocks.db.constants.RocksDbExceptionMessages.*;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.*;


@Getter
public class RocksDbModule implements Module<RocksDbModuleConfiguration, RocksDbModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static RocksDbModuleConfiguration rocksDbModule = context().getModule(ROCKS_DB_MODULE_ID, RocksDbModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private final static RocksDbModuleState rocksDbModuleState = context().getModuleState(ROCKS_DB_MODULE_ID, RocksDbModule::new);
    private final String id = ROCKS_DB_MODULE_ID;
    private final RocksDbModuleConfiguration defaultConfiguration = RocksDbModuleDefaultConfiguration.DEFAULT_CONFIGURATION;
    private RocksDbModuleState state;

    public static RocksDbModuleConfiguration rocksDbModule() {
        if (insideDefaultContext()) {
            return RocksDbModuleConfiguration.DEFAULT_CONFIGURATION;
        }
        return getRocksDbModule();
    }

    public static RocksDbModuleState rocksDbModuleState() {
        return getRocksDbModuleState();
    }

    @Override
    public void onLoad() {
        loadLibrary();
        try {
            state = new RocksDbModuleState(open(rocksDbModule().getOptions(), rocksDbModule().getPath()));
        } catch (RocksDBException e) {
            throw new RocksDbOperationException(OPEN_ERROR, e);
        }
    }

    @Override
    public void onUnload() {
        RocksDbModuleState state = rocksDbModuleState();
        if (isNull(state)) return;
        RocksDB db = state.getDb();
        if (nonNull(db)) {
            db.close();
        }
    }
}
