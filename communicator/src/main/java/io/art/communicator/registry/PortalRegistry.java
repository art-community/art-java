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
public class PortalRegistry {
    private final LazyProperty<ImmutableMap<Class<? extends Portal>, PortalContainer>> portals;

    public <T extends Portal> T getPortal(Class<T> portal) {
        return cast(portals.get().get(portal).portal);
    }

    public <T extends Communicator> CommunicatorProxy<T> getCommunicator(Class<? extends Portal> portalClass, Class<? extends Communicator> communicatorClass) {
        return cast(portals.get().get(portalClass).communicators.get(communicatorClass));
    }

    public ImmutableArray<? extends Portal> portals() {
        return portals.get().values().stream().map(container -> container.portal).collect(immutableArrayCollector());
    }

    public ImmutableArray<CommunicatorProxy<? extends Communicator>> communicators() {
        return portals.get().values().stream().flatMap(container -> container.communicators.values().stream()).collect(immutableArrayCollector());
    }

    public ImmutableArray<CommunicatorAction> actions() {
        return portals.get().values()
                .stream()
                .flatMap(container -> container.communicators.values()
                        .stream()
                        .flatMap(communicator -> communicator.getActions().values().stream()))
                .collect(immutableArrayCollector());
    }

    @RequiredArgsConstructor
    public static class PortalContainer {
        final ImmutableMap<Class<? extends Communicator>, CommunicatorProxy<? extends Communicator>> communicators;
        final Portal portal;
    }
}
