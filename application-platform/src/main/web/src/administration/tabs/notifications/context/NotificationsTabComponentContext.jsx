import {ComponentContext} from "../../../../framework/context/ComponentContext";
import {NotificationsTabComponentController} from "../controller/NotificationsTabComponentController";
import {NotificationsTabComponentStore} from "../store/NotificationsTabComponentStore";

export class NotificationsTabComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.controller = new NotificationsTabComponentController(this);
        this.store = new NotificationsTabComponentStore(component);
    }
}