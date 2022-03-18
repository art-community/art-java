package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.experimental.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@Public
@UtilityClass
public class ShardRequestFactory {
    public static ShardRequest shard(ShardAlgorithm algorithm, Tuple data) {
        return new ShardRequest(algorithm, data);
    }

    public static ShardRequest crc32(Tuple data) {
        return new ShardRequest(CRC_32, data);
    }
}
