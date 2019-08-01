package ru.art.rocks.db.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.rocksdb.RocksDB;
import ru.art.core.module.ModuleState;

@Getter
@AllArgsConstructor
public class RocksDbModuleState implements ModuleState {
    private final RocksDB db;
}
