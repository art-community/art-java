package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.experimental.*;
import static io.art.core.model.Tuple.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@Public
@UtilityClass
public class SharderFactory {
    public static <T> Sharder constantSharder(T value) {
        return constantSharder(value, CRC_32);
    }

    public static <T> Sharder constantSharder(T value, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(value));
    }
}
