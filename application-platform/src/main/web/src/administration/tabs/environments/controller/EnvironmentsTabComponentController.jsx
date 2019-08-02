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

import {ComponentController} from "../../../../framework/controller/ComponentController";
import {registry} from "../../../../framework/registry/Registry";
import {loadClusters, loadServers} from "../../../../service/AdministrationService";
import isEmpty from "licia/isEmpty";

export class EnvironmentsTabComponentController extends ComponentController {
    selectEnvironment = (environment) => {
        if (environment === registry.environmentsTab.store.selectedEnvironment) return;
        registry.environmentsTab.store.selectedEnvironment = environment;
        registry.environmentsTab.store.selectedCluster = null;
        registry.environmentsTab.store.selectedServer = null;
        loadClusters(environment, clusters => {
            if (isEmpty(clusters)) {
                registry.environmentsTab.store.apply();
                return
            }
            registry.environmentsTab.store.clusters = new Set(clusters);
            registry.environmentsTab.store.selectedCluster = clusters[0];
            loadServers(environment, clusters[0], servers => {
                if (isEmpty(servers)) {
                    registry.environmentsTab.store.apply();
                    return;
                }
                registry.environmentsTab.store.servers = new Set(servers);
                registry.environmentsTab.store.selectedServer = servers[0];
                if (registry.serverConfiguration) {
                    registry.serverConfiguration.controller.loadServerConfiguration(environment, clusters[0], servers[0]);
                }
                registry.environmentsTab.store.apply();
            });
        })
    };

    selectCluster = (cluster) => {
        if (cluster === registry.environmentsTab.store.selectedCluster) return;
        registry.environmentsTab.store.selectedCluster = cluster;
        registry.environmentsTab.store.selectedServer = null;
        loadServers(registry.environmentsTab.store.selectedEnvironment, cluster, servers => {
            if (isEmpty(servers)) {
                registry.environmentsTab.store.apply();
                return;
            }
            registry.environmentsTab.store.servers = new Set(servers);
            registry.environmentsTab.store.selectedServer = servers[0];
            if (registry.serverConfiguration) {
                registry.serverConfiguration.controller.loadServerConfiguration(registry.environmentsTab.store.selectedEnvironment, cluster, servers[0]);
            }
            registry.environmentsTab.store.apply();
        });
    };

    selectServer = (server) => {
        if (server === registry.environmentsTab.store.selectedServer) return;
        registry.environmentsTab.store.selectedServer = server;
        if (registry.serverConfiguration) {
            registry.serverConfiguration.controller.loadServerConfiguration(registry.environmentsTab.store.selectedEnvironment, registry.environmentsTab.store.selectedCluster, server);
        }
        registry.environmentsTab.store.apply();
    };


    getSelectedEnvironment = () => registry.environmentsTab.store.selectedEnvironment;
    getSelectedCluster = () => registry.environmentsTab.store.selectedCluster;
    getSelectedServer = () => registry.environmentsTab.store.selectedServer;
}