package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.storage.constants.StorageConstants.*;
import static io.art.core.model.Tuple.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@Public
public interface Sharders<T> {
    default <C> ShardFunction1<T, C> constant(C constant) {
        return ignore -> sharder(constant, CRC_32);
    }

    default <C> ShardFunction1<T, C> constant(C constant, ShardAlgorithm algorithm) {
        return ignore -> sharder(constant, algorithm);
    }

    default <P1> Sharder sharder(P1 p1) {
        return sharder(p1, CRC_32);
    }

    default <P1> Sharder sharder(P1 p1, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(p1));
    }

    default <P1, P2> Sharder sharder(P1 P1, P2 p2, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(P1, p2));
    }
}
