package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.experimental.*;
import static io.art.core.model.Tuple.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@Public
@UtilityClass
public class Sharding {
    public static <P1> Sharder sharder(P1 p1) {
        return sharder(p1, CRC_32);
    }

    public static <P1> Sharder sharder(P1 p1, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(p1));
    }

    public static <P1, P2> Sharder sharder(P1 P1, P2 p2, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(P1, p2));
    }
}
