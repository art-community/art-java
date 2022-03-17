package io.art.storage.sharder;

import io.art.core.annotation.*;
import io.art.storage.service.*;
import lombok.*;
import static io.art.core.model.Tuple.*;


@Public
@RequiredArgsConstructor
public class ShardProvider1<KeyType, ModelType, P1, ServiceType extends ShardService<KeyType, ModelType, ServiceType>> {
    private final ShardProvider<KeyType, ModelType, ServiceType> provider;

    public ServiceType by(P1 p1) {
        return provider.by(tuple(p1));
    }
}
