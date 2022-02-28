package io.art.tarantool.registry;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.tarantool.model.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import java.util.function.*;

@RequiredArgsConstructor
public class TarantoolSubscriptionRegistry {
    private final LazyProperty<ImmutableMap<ServiceMethodIdentifier, TarantoolSubscription>> subscriptions;

    public TarantoolSubscription get(ServiceMethodIdentifier method) {
        return subscriptions.get().get(method);
    }

    public void forEach(Consumer<TarantoolSubscription>  consumer) {
        subscriptions.get().values().forEach(consumer);
    }
}
