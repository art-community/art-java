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

import {registry} from "../../../../framework/registry/Registry";
import {loadEnvironments, saveEnvironments} from "../../../../service/AdministrationService";
import isEmpty from 'licia/isEmpty'

export const reactors = {
    onMount: () => loadEnvironments(environments => {
        if (isEmpty(environments)) return;
        registry.environmentsTab.store.environments = new Set(environments);
        registry.environmentsTab.store.apply()
    }),

    onSave: () => saveEnvironments(registry.environmentsTab.store.environments),

    onSelectEnvironment: (environment) => registry.environmentsTab.controller.selectEnvironment(environment),

    onDeleteEnvironment: () => {
        registry.environmentsTab.store.deleteEnvironment(registry.environmentsTab.store.selectedEnvironment);
        registry.environmentsTab.store.selectedEnvironment = null;
        registry.environmentsTab.store.selectedCluster = null;
        registry.environmentsTab.store.selectedServer = null;
        registry.environmentsTab.store.apply();
    },

    onAddEnvironment: () => {
        if(!registry.environmentsTab.store.addEnvironment(registry.environmentsTab.store.newEnvironment)) return;
        registry.environmentsTab.controller.selectEnvironment(registry.environmentsTab.store.newEnvironment);
    },

    onInputNewEnvironment: (environment) => registry.environmentsTab.store.newEnvironment = environment,


    onSelectCluster: (cluster) => registry.environmentsTab.controller.selectCluster(cluster),

    onDeleteCluster: () => {
        registry.environmentsTab.store.deleteCluster(registry.environmentsTab.store.selectedCluster);
        registry.environmentsTab.store.selectedCluster = null;
        registry.environmentsTab.store.selectedServer = null;
        registry.environmentsTab.store.apply();
    },

    onAddCluster: () => {
        if(!registry.environmentsTab.store.addCluster(registry.environmentsTab.store.newCluster)) return;
        registry.environmentsTab.controller.selectCluster(registry.environmentsTab.store.newCluster);
    },

    onInputNewCluster: (cluster) => registry.environmentsTab.store.newCluster= cluster,

    onSelectServer: (server) => registry.environmentsTab.controller.selectServer(server),

    onDeleteServer: () => {
        registry.environmentsTab.store.deleteServer(registry.environmentsTab.store.selectedServer);
        registry.environmentsTab.store.selectedServer = null;
        registry.environmentsTab.store.apply();
    },

    onAddServer: () => {
        if(!registry.environmentsTab.store.addServer(registry.environmentsTab.store.newServer)) return;
        registry.environmentsTab.controller.selectServer(registry.environmentsTab.store.newServer);
    },

    onInputNewServer: (server) => registry.environmentsTab.store.newServer = server
};
