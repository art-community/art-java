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
import io.art.storage.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.refresher.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.service.*;
import io.art.tarantool.storage.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class TarantoolInitializer implements ModuleInitializer<TarantoolModuleConfiguration, TarantoolModuleConfiguration.Configurator, TarantoolModule> {
    private final TarantoolCommunicatorConfigurator communicatorConfigurator = new TarantoolCommunicatorConfigurator();
    private final Map<String, LazyProperty<TarantoolSpaceService<?, ?>>> spaceServices = map();

    public TarantoolInitializer storage(Class<? extends Storage> storageClass) {
        return storage(storageClass, identity());
    }

    public TarantoolInitializer storage(Class<? extends Storage> storageClass, UnaryOperator<TarantoolStorageConfigurator> configurator) {
        this.communicatorConfigurator.storage(storageClass, configurator);
        return this;
    }

    public TarantoolInitializer space(Class<? extends Storage> storageClass, Class<? extends Space> spaceClass) {
        return space(storageClass, spaceClass, UnaryOperator.identity());
    }

    public TarantoolInitializer space(Class<? extends Storage> storageClass, Class<? extends Space> spaceClass, UnaryOperator<TarantoolStorageConfigurator> configurator) {
        storage(storageClass, configurator);
        spaceServices.put(idByDot(spaceClass), lazy(() -> new TarantoolSpaceService<>(definition(spaceClass), () -> new TarantoolStorage(tarantoolModule().configuration().getStorages().get(idByDash(storageClass))))));
        return this;
    }

    @Override
    public TarantoolModuleConfiguration initialize(TarantoolModule module) {
        Initial initial = new Initial(module.getRefresher());

        initial.storages = communicatorConfigurator.connectors();
        initial.communicator = communicatorConfigurator.configureCommunicator(lazy(() -> tarantoolModule().configuration().getCommunicator()), initial.communicator);
        initial.services = new TarantoolServiceRegistry(lazy(() -> spaceServices.entrySet().stream().collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().get()))));

        return initial;
    }

    @Getter
    public static class Initial extends TarantoolModuleConfiguration {
        private ImmutableMap<String, TarantoolStorageConfiguration> storages = super.getStorages();
        private CommunicatorConfiguration communicator = super.getCommunicator();
        private TarantoolServiceRegistry services = super.getServices();

        public Initial(TarantoolModuleRefresher refresher) {
            super(refresher);
        }
    }
}
