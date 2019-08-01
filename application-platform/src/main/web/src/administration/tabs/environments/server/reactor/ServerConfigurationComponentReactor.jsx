import {registry} from "../../../../../framework/registry/Registry";

export const reactors = {
    onMount: () => registry.serverConfiguration.controller.loadServerConfiguration(registry.environmentsTab.controller.getSelectedEnvironment(),
        registry.environmentsTab.controller.getSelectedCluster(),
        registry.environmentsTab.controller.getSelectedServer()),

    onServerPropertyInput: (name, value, applyStore = false) => {
        registry.serverConfiguration.store.setServerProperty(name, value);
        if (applyStore) {
            registry.serverConfiguration.store.apply()
        }
    },
};