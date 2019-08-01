package ru.art.rocks.db.constants;

public interface RocksDbExceptionMessages {
    String OPEN_ERROR = "Occurred error during opening RocksDb";
    String PUT_ERROR = "Occurred error during putting value into RocksDb";
    String MERGE_ERROR = "Occurred error during merging value into RocksDb";
    String GET_ERROR = "Occurred error during getting value from RocksDb";
    String DELETE_ERROR = "Occurred error during deleting value from RocksDb";
    String PROTOBUF_PARSING_ERROR = "Occurred error during parsing protobuf value from RocksDb";
}
