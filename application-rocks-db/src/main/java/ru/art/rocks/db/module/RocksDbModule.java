package ru.art.rocks.db.module;

import lombok.Getter;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import ru.art.core.module.Module;
import ru.art.rocks.db.configuration.RocksDbModuleConfiguration;
import ru.art.rocks.db.configuration.RocksDbModuleConfiguration.RocksDbModuleDefaultConfiguration;
import ru.art.rocks.db.exception.RocksDbOperationException;
import ru.art.rocks.db.state.RocksDbModuleState;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.rocksdb.RocksDB.loadLibrary;
import static org.rocksdb.RocksDB.open;
import static ru.art.core.context.Context.context;
import static ru.art.rocks.db.constants.RocksDbExceptionMessages.OPEN_ERROR;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.ROCKS_DB_MODULE_ID;


@Getter
public class RocksDbModule implements Module<RocksDbModuleConfiguration, RocksDbModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static RocksDbModuleConfiguration rocksDbModule = context().getModule(ROCKS_DB_MODULE_ID, new RocksDbModule());
    @Getter(lazy = true, value = PRIVATE)
    private final static RocksDbModuleState rocksDbModuleState = context().getModuleState(ROCKS_DB_MODULE_ID, new RocksDbModule());
    private final RocksDbModuleConfiguration defaultConfiguration = new RocksDbModuleDefaultConfiguration();
    private final String id = ROCKS_DB_MODULE_ID;
    private RocksDbModuleState state;

    public static RocksDbModuleConfiguration rocksDbModule() {
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
