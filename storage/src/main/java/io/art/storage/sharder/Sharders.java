package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.storage.constants.StorageConstants.*;
import static io.art.core.model.Tuple.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;

@Public
public interface Sharders<T> {
    default <C> Sharder1<T, C> constant(C constant) {
        return ignore -> sharder(constant, CRC_32);
    }

    default <C> Sharder1<T, C> constant(C constant, ShardAlgorithm algorithm) {
        return ignore -> sharder(constant, algorithm);
    }

    default <P1> ShardRequest sharder(Tuple tuple) {
        return sharder(CRC_32, tuple);
    }

    default <P1> ShardRequest sharder(ShardAlgorithm algorithm, Tuple tuple) {
        return new ShardRequest(algorithm, tuple);
    }

    default <P1> ShardRequest sharder(P1 p1) {
        return sharder(CRC_32, p1);
    }

    default <P1> ShardRequest sharder(ShardAlgorithm algorithm, P1 p1) {
        return new ShardRequest(algorithm, tuple(p1));
    }

    default <P1, P2> ShardRequest sharder(P1 p1, P2 p2) {
        return sharder(CRC_32, p1, p2);
    }

    default <P1, P2> ShardRequest sharder(ShardAlgorithm algorithm, P1 P1, P2 p2) {
        return new ShardRequest(algorithm, tuple(P1, p2));
    }

    default <P1, P2, P3> ShardRequest sharder(P1 p1, P2 p2, P3 p3) {
        return sharder(CRC_32, p1, p2, p3);
    }

    default <P1, P2, P3> ShardRequest sharder(ShardAlgorithm algorithm, P1 P1, P2 p2, P3 p3) {
        return new ShardRequest(algorithm, tuple(P1, p2, p3));
    }

    default <P1, P2, P3, P4> ShardRequest sharder(P1 p1, P2 p2, P3 p3, P4 p4) {
        return sharder(CRC_32, p1, p2, p3, p4);
    }

    default <P1, P2, P3, P4> ShardRequest sharder(ShardAlgorithm algorithm, P1 P1, P2 p2, P3 p3, P4 p4) {
        return new ShardRequest(algorithm, tuple(P1, p2, p3, p4));
    }

    default <P1, P2, P3, P4, P5> ShardRequest sharder(P1 p1, P2 p2, P3 p3, P4 p4, P5 p5) {
        return sharder(CRC_32, p1, p2, p3, p4, p5);
    }

    default <P1, P2, P3, P4, P5> ShardRequest sharder(ShardAlgorithm algorithm, P1 P1, P2 p2, P3 p3, P4 p4, P5 p5) {
        return new ShardRequest(algorithm, tuple(P1, p2, p3, p4, p5));
    }
}
