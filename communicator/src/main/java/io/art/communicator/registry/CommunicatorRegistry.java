package io.art.communicator.registry;

import io.art.communicator.*;
import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;

@RequiredArgsConstructor
public class CommunicatorRegistry {
    private final LazyProperty<ImmutableMap<CommunicatorKey, CommunicatorProxy<? extends Communicator>>> communicators;

    public <T extends Communicator> CommunicatorProxy<T> getCommunicator(ConnectorIdentifier connector, Class<T> communicatorClass) {
        return cast(communicators.get().get(new CommunicatorKey(connector, communicatorClass)));
    }

    public ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators() {
        return communicators.get().values().stream().collect(immutableArrayCollector());
    }

    public ImmutableArray<CommunicatorAction> actions() {
        return communicators.get()
                .values()
                .stream()
                .flatMap(communicator -> communicator.getActions().values().stream()).collect(immutableArrayCollector());
    }
}
