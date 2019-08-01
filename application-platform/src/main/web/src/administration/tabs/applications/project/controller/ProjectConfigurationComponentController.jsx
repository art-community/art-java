import {ComponentController} from "../../../../../framework/controller/ComponentController";
import {loadEnvironments, loadProject, loadServersByEnvironments} from "../../../../../service/AdministrationService";
import {registry} from "../../../../../framework/registry/Registry";
import isEmpty from 'licia/isEmpty'
import contain from "licia/contain";

export class ProjectConfigurationComponentController extends ComponentController {
    loadProjectConfiguration = (application, group, project) => {
        loadProject(application, group, project, project => {
            if (isEmpty(project)) {
                registry.projectConfiguration.store.project = {};
                registry.projectConfiguration.store.apply();
                return
            }
            registry.projectConfiguration.store.project = project;
            loadEnvironments(environments => {
                if (isEmpty(environments)) {
                    registry.projectConfiguration.store.environments = new Set();
                    registry.projectConfiguration.store.servers = new Set();
                    registry.projectConfiguration.store.project.module.availableEnvironments = [];
                    registry.projectConfiguration.store.project.module.availableServers = [];
                    registry.projectConfiguration.store.apply();
                    return
                }
                registry.projectConfiguration.store.environments = new Set(environments);
                const newAvailableEnvironments = [];
                registry.projectConfiguration.store.project.module.availableEnvironments.forEach(environment => {
                    if (contain(environments, environment)) {
                        newAvailableEnvironments.push(environment);
                    }
                });
                registry.projectConfiguration.store.project.module.availableEnvironments = newAvailableEnvironments;
                loadServersByEnvironments(environments, servers => {
                    if (isEmpty(servers)) {
                        registry.projectConfiguration.store.servers = new Set();
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
                    registry.projectConfiguration.store.apply();
                })
            });
        });
    }
}