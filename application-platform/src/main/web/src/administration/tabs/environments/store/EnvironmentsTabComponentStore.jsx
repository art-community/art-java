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

import {ComponentStore} from "../../../../framework/store/ComponentStore";
import isEmpty from "licia/isEmpty";

export class EnvironmentsTabComponentStore extends ComponentStore {
    environments = new Set();
    servers = new Set();
    clusters = new Set();
    newEnvironment;
    newCluster;
    newServer;
    selectedEnvironment;
    selectedServer;
    selectedCluster;


    addEnvironment = (environment) => {
        if (isEmpty(environment) || this.environments.has(environment)) return false;
        this.environments.add(environment);
        return true
    };

    deleteEnvironment = (environment) => {
        if (isEmpty(environment) || !this.environments.has(environment)) return false;
        this.environments.delete(environment);
        return true
    };

    noEnvironments = () => this.environments.size === 0;

    addServer = (server) => {
        if (isEmpty(server) || this.servers.has(server)) return false;
        this.servers.add(server);
        return true
    };

    deleteServer = (server) => {
        if (isEmpty(server) || !this.servers.has(server)) return false;
        this.servers.delete(server);
        return true
    };

    noServers = () => this.servers.size === 0;

    addCluster = (cluster) => {
        if (isEmpty(cluster) || this.clusters.has(cluster)) return false;
        this.clusters.add(cluster);
        return true
    };

    deleteCluster = (cluster) => {
        if (isEmpty(cluster) || !this.clusters.has(cluster)) return false;
        this.clusters.delete(cluster);
        return true
    };

    noClusters = () => this.clusters.size === 0;

    toState = () => ({
        environments: Array.from(this.environments),
        servers: Array.from(this.servers),
        clusters: Array.from(this.clusters),
        selectedEnvironment: this.selectedEnvironment,
        selectedServer: this.selectedServer,
        selectedCluster: this.selectedCluster
    });

}
