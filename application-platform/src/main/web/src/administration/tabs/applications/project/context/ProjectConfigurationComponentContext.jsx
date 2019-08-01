import {ProjectConfigurationComponentStore} from "../store/ProjectConfigurationComponentStore";
import {ProjectConfigurationComponentController} from "../controller/ProjectConfigurationComponentController";
import {ComponentContext} from "../../../../../framework/context/ComponentContext";

export class ProjectConfigurationComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.controller = new ProjectConfigurationComponentController(this);
        this.store = new ProjectConfigurationComponentStore(component);
    }
}