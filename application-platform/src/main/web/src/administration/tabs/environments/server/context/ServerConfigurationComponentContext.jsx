import {ComponentContext} from "../../../../../framework/context/ComponentContext";
import {ServerConfigurationComponentStore} from "../store/ServerConfigurationComponentStore";
import {ServerConfigurationComponentController} from "../controller/ServerConfigurationComponentController";

export class ServerConfigurationComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.controller = new ServerConfigurationComponentController(this);
        this.store = new ServerConfigurationComponentStore(component);
    }
}