package io.art.storage.sharder;

import lombok.experimental.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;
import static io.art.storage.sharder.SharderFactory.*;

@UtilityClass
public class SharderFunctions {
    public static <T, M> ShardFunction constantSharder(T constant) {
        return ignore -> sharderFactory().sharder(constant, CRC_32);
    }
}
