package io.art.tarantool.module;

import io.art.communicator.*;
import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.storage.*;
import io.art.tarantool.configuration.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.tarantool.factory.TarantoolCommunicationFactory.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolCommunicatorConfigurator extends CommunicatorConfigurator<TarantoolCommunicatorConfigurator> {
    private final Map<String, TarantoolStorageConfiguration> storages = map();

    public TarantoolCommunicatorConfigurator storage(Class<? extends Storage> storageClass) {
        return storage(storageClass, identity());
    }

    public TarantoolCommunicatorConfigurator storage(Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConfigurator> configurator) {
        TarantoolStorageConfigurator storageConfigurator = configurator.apply(new TarantoolStorageConfigurator(idByDash(storageClass)));
        TarantoolStorageConfiguration configuration = storageConfigurator.configure();
        storages.put(idByDash(storageClass), configuration);
        Function<Class<? extends Communicator>, Communicator> communicatorFunction = spaceCommunicator -> tarantoolModule()
                .configuration()
                .getCommunicator()
                .getPortals()
                .getCommunicator(storageClass, spaceCommunicator)
                .getCommunicator();
        Function<CommunicatorActionIdentifier, Communication> communicationFunction = identifier -> createConfiguredTarantoolCommunication(configuration);
        registerPortal(storageClass, communicatorFunction, communicationFunction);
        return this;
    }

    ImmutableMap<String, TarantoolStorageConfiguration> storages() {
        return immutableMapOf(storages);
    }

    CommunicatorConfiguration configureCommunicator(LazyProperty<CommunicatorConfiguration> configurationProvider, CommunicatorConfiguration current) {
        return configure(configurationProvider, current);
    }
}
