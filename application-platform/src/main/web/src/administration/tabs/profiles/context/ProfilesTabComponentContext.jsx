import {ComponentContext} from "../../../../framework/context/ComponentContext";
import {ProfilesTabComponentStore} from "../store/ProfilesTabComponentStore";

export class ProfilesTabComponentContext extends ComponentContext {
    constructor(component) {
        super();
        this.store = new ProfilesTabComponentStore(component);
    }
}