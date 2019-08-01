export class ComponentReactor {
    constructor(context, reaction) {
        this.context = context;
        this.reaction = reaction;
    }
}

export const lazyReactor = (context, reactors, reaction) => {
    const reactor = reactors[context];
    if (reactor !== undefined && reactor != null) {
        return reactor;
    }
    return new ComponentReactor(context, reaction)
};
