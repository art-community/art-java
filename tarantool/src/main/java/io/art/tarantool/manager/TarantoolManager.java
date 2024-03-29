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

package io.art.tarantool.manager;


import io.art.communicator.action.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.model.*;
import io.art.tarantool.registry.*;

public class TarantoolManager {
    private final TarantoolModuleConfiguration configuration;

    public TarantoolManager(TarantoolModuleConfiguration configuration) {
        this.configuration = configuration;
    }

    public void initialize() {
        for (CommunicatorAction communicatorAction : configuration.getCommunicator().getCommunicators().actions()) {
            communicatorAction.initialize();
        }
        for (TarantoolStorageRegistry registry : configuration.getStorageRegistries().get().values()) {
            registry.getConnector().initialize();
        }
    }

    public void dispose() {
        for (CommunicatorAction communicatorAction : configuration.getCommunicator().getCommunicators().actions()) {
            communicatorAction.dispose();
        }
        LazyProperty<ImmutableMap<String, TarantoolStorageRegistry>> registries = configuration.getStorageRegistries();
        if (registries.initialized()) {
            for (TarantoolStorageRegistry tarantoolStorageRegistry : registries.get().values()) {
                tarantoolStorageRegistry.getConnector().dispose();
            }
        }
        configuration.getSubscriptions().forEach(TarantoolSubscription::cancel);
    }
}
