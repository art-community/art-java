/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import {registry} from "../../../../../framework/registry/Registry";
import {loadServersByEnvironments} from "../../../../../service/AdministrationService";
import isEmpty from 'licia/isEmpty'
import contain from 'licia/contain'

export const reactors = {
    onMount: () => registry.projectConfiguration.controller.loadProjectConfiguration(registry.applicationsTab.controller.getSelectedApplication(),
        registry.applicationsTab.controller.getSelectedProjectGroup(),
        registry.applicationsTab.controller.getSelectedProject()),

    onSelectEnvironments: (environments) => {
        registry.projectConfiguration.store.setProjectProperty('module.availableEnvironments', environments);
        loadServersByEnvironments(environments, servers => {
            if (isEmpty(servers)) {
                registry.projectConfiguration.store.project.module.availableServers = [];
                registry.projectConfiguration.store.apply();
                return
            }
            const newAvailableServers = [];
            registry.projectConfiguration.store.project.module.availableServers.forEach(server => {
                if (contain(servers, server)) {
                    newAvailableServers.push(server);
                }
            });
            registry.projectConfiguration.store.project.module.availableServers = newAvailableServers;
            registry.projectConfiguration.store.servers = new Set(servers);
            registry.projectConfiguration.store.apply()
        })
    },

    onProjectPropertyInput: (name, value, applyStore = false) => {
        registry.projectConfiguration.store.setProjectProperty(name, value);
        if (applyStore) {
            registry.projectConfiguration.store.apply()
        }
    },
};