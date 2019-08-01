import {ComponentContext} from "../../../../framework/context/ComponentContext";
import {UsersTabComponentStore} from "../store/UsersTabComponentStore";

export class UsersTabComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.store = new UsersTabComponentStore(component);
    }
}