package io.art.storage.sharder;

import io.art.storage.constants.StorageConstants.*;
import lombok.experimental.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@UtilityClass
public class SharderFunctions {
    public static <T, M> ShardFunctionOperator1<M, T> constantSharder(T constant) {
        return (factory, parameter) -> ignore -> factory.sharder(CRC_32, constant);
    }

    public static <T, M> ShardFunctionOperator1<M, T> constantSharder(T constant, ShardAlgorithm algorithm) {
        return (factory, parameter) -> ignore -> factory.sharder(algorithm, constant);
    }
}
