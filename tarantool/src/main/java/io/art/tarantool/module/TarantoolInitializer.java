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
import io.art.core.property.*;
import io.art.server.configuration.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.registry.*;
import lombok.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.tarantool.module.TarantoolModule.*;
import java.util.function.*;

@Public
public class TarantoolInitializer implements ModuleInitializer<TarantoolModuleConfiguration, TarantoolModuleConfiguration.Configurator, TarantoolModule> {
    private final TarantoolStoragesConfigurator storagesConfigurator = new TarantoolStoragesConfigurator();
    private final TarantoolSubscriptionsConfigurator subscriptionsConfigurator = new TarantoolSubscriptionsConfigurator();

    public TarantoolInitializer storages(UnaryOperator<TarantoolStoragesConfigurator> configurator) {
        configurator.apply(storagesConfigurator);
        return this;
    }

    public TarantoolInitializer subscriptions(UnaryOperator<TarantoolSubscriptionsConfigurator> configurator) {
        configurator.apply(subscriptionsConfigurator);
        return this;
    }


    @Override
    public TarantoolModuleConfiguration initialize(TarantoolModule module) {
        Initial initial = new Initial(module.getRefresher());

        initial.storageConfigurations = storagesConfigurator.storageConfigurations();
        initial.storages = lazy(storagesConfigurator::createStorages);
        initial.communicator = storagesConfigurator.createCommunicatorConfiguration(initial.communicator);
        initial.server = subscriptionsConfigurator.configureServer(lazy(() -> tarantoolModule().configuration().getServer()), initial.server);
        initial.subscriptions = new TarantoolSubscriptionRegistry(lazy(subscriptionsConfigurator::configureSubscriptions));

        return initial;
    }

    @Getter
    public static class Initial extends TarantoolModuleConfiguration {
        private ImmutableMap<String, TarantoolStorageConfiguration> storageConfigurations = super.getStorageConfigurations();
        private LazyProperty<ImmutableMap<String, TarantoolStorageRegistry>> storages = lazy(super::storageRegistries);
        private CommunicatorConfiguration communicator = super.getCommunicator();
        private ServerConfiguration server = super.getServer();
        private TarantoolSubscriptionRegistry subscriptions = super.getSubscriptions();

        public Initial(TarantoolModuleRefresher refresher) {
            super(refresher);
        }
    }
}
