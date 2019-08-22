/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.configurator.service;

import ru.art.config.remote.api.specification.*;
import ru.art.configurator.api.entity.*;
import ru.art.core.checker.*;
import ru.art.entity.*;
import java.util.*;

import static ru.art.configurator.api.entity.Configuration.*;
import static ru.art.configurator.constants.ConfiguratorDbConstants.*;
import static ru.art.configurator.dao.ConfiguratorDao.*;
import static ru.art.configurator.factory.RemoteConfigCommunicationSpecificationsFactory.*;
import static ru.art.configurator.provider.ApplicationModulesParametersProvider.*;
import static ru.art.entity.Entity.*;


public interface ConfiguratorService {
    static void uploadConfiguration(Configuration configuration) {
        saveConfig(configuration);
    }

    static void uploadApplicationConfiguration(Configuration configuration) {
        saveApplicationConfiguration(configuration.getConfiguration());
    }

    static void uploadProfileConfiguration(ProfileConfiguration configuration) {
        saveProfileConfiguration(configuration.getProfileId(), configuration.getConfiguration());
    }

    static void uploadModuleConfiguration(ModuleConfiguration configuration) {
        saveModuleConfiguration(configuration.getModuleKey(), configuration.getConfiguration());
    }


    static Configuration getConfiguration(ModuleKey moduleKey) {
        ConfigurationBuilder applicationConfigurationBuilder = builder().configuration(entityBuilder().build());
        getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .ifPresent(applicationConfigurationBuilder::configuration);
        return applicationConfigurationBuilder.build();
    }

    static Set<ModuleKey> getProfiles() {
        return getProfileKeys();
    }

    static Set<ModuleKey> getModules() {
        return getModuleKeys();
    }

    static Configuration getApplicationConfiguration() {
        return builder()
                .configuration(getConfig(APPLICATION).map(Value::asEntity).orElse(entityBuilder().build()))
                .build();
    }


    static void applyModuleConfiguration(ModuleKey moduleKey) {
        getApplicationModuleParameters(moduleKey)
                .filter(CheckerForEmptiness::isNotEmpty)
                .flatMap(parameters -> createRemoteConfigCommunicationSpecification(parameters, moduleKey))
                .ifPresent(RemoteConfigCommunicationSpecification::applyConfiguration);
    }
}