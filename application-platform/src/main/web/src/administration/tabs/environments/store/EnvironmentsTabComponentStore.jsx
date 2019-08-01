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
