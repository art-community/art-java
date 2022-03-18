package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.core.model.*;
import lombok.*;
import java.util.function.*;

@Public
@Getter
@AllArgsConstructor
public class ShardFunction {
    private final Function<Tuple, Sharder> functor;
}
