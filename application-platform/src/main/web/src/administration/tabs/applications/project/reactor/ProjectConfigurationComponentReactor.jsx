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