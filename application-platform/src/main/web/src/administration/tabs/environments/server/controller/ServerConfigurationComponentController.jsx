import {ComponentController} from "../../../../../framework/controller/ComponentController";
import {loadServer} from "../../../../../service/AdministrationService";
import {registry} from "../../../../../framework/registry/Registry";
import isEmpty from "licia/isEmpty";

export class ServerConfigurationComponentController extends ComponentController {
    loadServerConfiguration = (environment, cluster, server) => {
        loadServer(environment, cluster, server, server => {
            if (isEmpty(server)) {
                registry.serverConfiguration.store.server = {};
                registry.serverConfiguration.store.apply();
                return
            }
            registry.serverConfiguration.store.server = server;
            registry.serverConfiguration.store.apply();
        })
    }
}