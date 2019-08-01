package ru.art.rocks.db.constants;

public interface RocksDbModuleConstants {
    String ROCKS_DB_MODULE_ID = "ROCKS_DB_MODULE";
    String DEFAULT_PATH_TO_DB = "rocks.db";
    String ROCKS_DB_KEY_DELIMITER = ":";
    String ROCKS_DB_LIST_DELIMITER = ",";

    interface DefaultOptions {
        int DEFAULT_KEY_PREFIX_BYTES = 2048;
        @SuppressWarnings("SpellCheckingInspection")
        String DEFAULT_MERGE_OPERATOR = "stringappend";
    }
}
