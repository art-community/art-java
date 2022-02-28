/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.tarantool.module;

import io.art.communicator.configuration.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.meta.model.*;
import io.art.server.configuration.*;
import io.art.storage.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.registry.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolInitializer implements ModuleInitializer<TarantoolModuleConfiguration, TarantoolModuleConfiguration.Configurator, TarantoolModule> {
    private final TarantoolCommunicatorConfigurator communicatorConfigurator = new TarantoolCommunicatorConfigurator();
    private final TarantoolServicesConfigurator servicesConfigurator = new TarantoolServicesConfigurator();
    private final TarantoolSubscriptionsConfigurator subscriptionsConfigurator = new TarantoolSubscriptionsConfigurator();

    public TarantoolInitializer storage(Class<? extends Storage> storageClass) {
        return storage(storageClass, identity());
    }

    public TarantoolInitializer storage(Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConfigurator> configurator) {
        communicatorConfigurator.storage(storageClass, configurator);
        return this;
    }

    public TarantoolInitializer subscribe(UnaryOperator<TarantoolSubscriptionsConfigurator> configurator) {
        configurator.apply(subscriptionsConfigurator);
        return this;
    }

    public <C, M extends MetaClass<C>> TarantoolInitializer space(Class<? extends Storage> storageClass, Class<C> spaceClass, Supplier<MetaField<M, ?>> idField) {
        servicesConfigurator.space(storageClass, spaceClass, idField);
        return this;
    }

    @Override
    public TarantoolModuleConfiguration initialize(TarantoolModule module) {
        Initial initial = new Initial(module.getRefresher());

        initial.storageConfigurations = communicatorConfigurator.storages();
        initial.storageClients = initial.storageConfigurations.entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> new TarantoolClientRegistry(entry.getValue())));

        initial.communicator = communicatorConfigurator.configure(lazy(() -> tarantoolModule().configuration().getCommunicator()), initial.communicator);
        initial.server = subscriptionsConfigurator.configureServer(lazy(() -> tarantoolModule().configuration().getServer()), initial.server);

        initial.services = servicesConfigurator.configure();
        initial.subscriptions = new TarantoolSubscriptionRegistry(lazy(subscriptionsConfigurator::configureSubscriptions));

        return initial;
    }

    @Getter
    public static class Initial extends TarantoolModuleConfiguration {
        private ImmutableMap<String, TarantoolStorageConfiguration> storageConfigurations = super.getStorageConfigurations();
        private ImmutableMap<String, TarantoolClientRegistry> storageClients = super.getStorageClients();

        private CommunicatorConfiguration communicator = super.getCommunicator();
        private ServerConfiguration server = super.getServer();

        private TarantoolServiceRegistry services = super.getServices();
        private TarantoolSubscriptionRegistry subscriptions = super.getSubscriptions();

        public Initial(TarantoolModuleRefresher refresher) {
            super(refresher);
        }
    }
}
