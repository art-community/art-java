package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.storage.constants.StorageConstants.*;
import lombok.*;

@Public
@Getter
@AllArgsConstructor
public class Sharder {
    private final ShardAlgorithm algorithm;
    private final Tuple key;
}
