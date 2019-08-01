package ru.adk.rocks.db.constants;

public interface RocksDbLoggingMessages {
    String MERGE_OPERATION = "Executing MERGE operation in Rocks DB. Merging bucket with key: ''{0}'' and value: ''{1}''";
    String PUT_OPERATION = "Executing PUT operation in Rocks DB. Putting bucket with key: ''{0}'' and value: ''{1}''";
    String DELETE_OPERATION = "Executing DELETE operation in Rocks DB. Deleting bucket with key: ''{0}''";
    String DELETE_BY_PREFIX_OPERATION = "Executing DELETE_BY_PREFIX operation in Rocks DB. Deleting bucket where key starts with: ''{0}''";
    String GET_START_OPERATION = "Executing GET operation in Rocks DB. Getting bucket with key: ''{0}''";
    String GET_END_OPERATION = "Executed GET operation in Rocks DB. Got value ''{0}'' bucket with key: ''{1}''";
}
