export class ComponentStore {
    constructor(component) {
        this.component = component;
    }

    apply = () => this.component.setState(this.toState());

    toState;
}
