export let orElse = (predicate, ifTrue, orElse) => {
    if (predicate) return ifTrue;
    return orElse;
};