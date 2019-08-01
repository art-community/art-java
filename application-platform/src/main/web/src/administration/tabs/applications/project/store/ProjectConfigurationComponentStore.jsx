import {ComponentStore} from "../../../../../framework/store/ComponentStore";
import {safeSet} from "licia";

export class ProjectConfigurationComponentStore extends ComponentStore {
    project = {};
    environments = new Set();
    servers = new Set();

    setProjectProperty = (name, value) => {
        safeSet(this.project, name, value)
    };

    toState = () => ({
        project: this.project,
        environments: Array.from(this.environments),
        servers: Array.from(this.servers)
    })
}