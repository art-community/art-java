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
import io.art.storage.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.storage.*;
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

    public TarantoolInitializer storage(Class<? extends Storage> storageClass) {
        return storage(storageClass, identity());
    }

    public TarantoolInitializer storage(Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConfigurator> configurator) {
        communicatorConfigurator.storage(storageClass, configurator);
        return this;
    }

    public TarantoolInitializer space(Class<? extends Storage> storageClass, Class<?> spaceClass, Supplier<MetaField<MetaClass<?>, ?>> idField) {
        servicesConfigurator.space(storageClass, spaceClass, idField);
        return this;
    }

    @Override
    public TarantoolModuleConfiguration initialize(TarantoolModule module) {
        Initial initial = new Initial(module.getRefresher());

        initial.storageConfigurations = communicatorConfigurator.storages();
        initial.storages = initial.storageConfigurations.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, entry -> new TarantoolStorage(entry.getValue())));
        initial.communicator = communicatorConfigurator.configureCommunicator(lazy(() -> tarantoolModule().configuration().getCommunicator()), initial.communicator);
        initial.services = servicesConfigurator.configure();

        return initial;
    }

    @Getter
    public static class Initial extends TarantoolModuleConfiguration {
        private ImmutableMap<String, TarantoolStorageConfiguration> storageConfigurations = super.getStorageConfigurations();
        private CommunicatorConfiguration communicator = super.getCommunicator();
        private TarantoolServiceRegistry services = super.getServices();
        private ImmutableMap<String, TarantoolStorage> storages = super.getStorages();

        public Initial(TarantoolModuleRefresher refresher) {
            super(refresher);
        }
    }
}
