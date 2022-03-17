package io.art.storage.sharder;

import io.art.storage.constants.StorageConstants.*;
import lombok.experimental.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;
import static io.art.storage.sharder.Sharding.*;

@UtilityClass
public class SharderFunctions {
    public static <T, M> ShardFunction constantSharder(T constant) {
        return new ShardFunction(ignore -> sharder(constant, CRC_32));
    }

    public static <T, M> ShardFunction constantSharder(T constant, ShardAlgorithm algorithm) {
        return new ShardFunction(ignore -> sharder(constant, algorithm));
    }
}
