import {ComponentStore} from "../../../../../framework/store/ComponentStore";
import {safeSet} from "licia";

export class ServerConfigurationComponentStore extends ComponentStore {
    server = {};

    setServerProperty = (name, value) => {
        safeSet(this.server, name, value)
    };

    toState = () => ({server: this.server});

}
