package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@Public
@Getter
@AllArgsConstructor
public class ShardRequestFactory {
    public ShardRequest shard(ShardAlgorithm algorithm, Tuple data) {
        return new ShardRequest(algorithm, data);
    }

    public ShardRequest crc32(Tuple data) {
        return new ShardRequest(CRC_32, data);
    }
}
