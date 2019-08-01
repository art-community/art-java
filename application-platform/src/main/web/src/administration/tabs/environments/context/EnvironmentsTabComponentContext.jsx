import {ComponentContext} from "../../../../framework/context/ComponentContext";
import {EnvironmentsTabComponentStore} from "../store/EnvironmentsTabComponentStore";
import {EnvironmentsTabComponentController} from "../controller/EnvironmentsTabComponentController";

export class EnvironmentsTabComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.controller = new EnvironmentsTabComponentController(this);
        this.store = new EnvironmentsTabComponentStore(component);
    }
}