package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.*;
import static io.art.core.model.Tuple.*;
import static io.art.storage.constants.StorageConstants.ShardAlgorithm.*;
import static lombok.AccessLevel.*;

@Public
@NoArgsConstructor(access = PRIVATE)
public class SharderFactory {
    public <P1> Sharder sharder(P1 p1) {
        return sharder(p1, CRC_32);
    }

    public <P1> Sharder sharder(P1 p1, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(p1));
    }

    public <P1, P2> Sharder sharder(P1 P1, P2 p2, ShardAlgorithm algorithm) {
        return new Sharder(algorithm, tuple(P1, p2));
    }

    private static final SharderFactory sharderFactory = new SharderFactory();

    public static SharderFactory sharderFactory() {
        return sharderFactory;
    }
}
