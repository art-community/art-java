package ru.adk.rocks.db.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.rocksdb.RocksDB;
import ru.adk.core.module.ModuleState;

@Getter
@AllArgsConstructor
public class RocksDbModuleState implements ModuleState {
    private final RocksDB db;
}
