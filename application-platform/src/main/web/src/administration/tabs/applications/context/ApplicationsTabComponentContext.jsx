import {ComponentContext} from "../../../../framework/context/ComponentContext";
import {ApplicationsTabComponentStore} from "../store/ApplicationsTabComponentStore";
import {ApplicationsTabComponentController} from "../controller/ApplicationsTabComponentController";

export class ApplicationsTabComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.controller = new ApplicationsTabComponentController(this);
        this.store = new ApplicationsTabComponentStore(component);
    }
}