Set.prototype.mapToArray = function <T, R>(this: Set<T>, functor: (value: T) => R): R[] {
    return Array.from(this).map(functor);
};

Set.prototype.addValue = function <T>(this: Set<T>, value: T): Set<T> {
    this.add(value);
    return new Set<T>(this);
};

Set.prototype.deleteValue = function <T>(this: Set<T>, value: T): Set<T> {
    this.delete(value);
    return new Set<T>(this);
};

Map.prototype.mapValuesToArray = function <K, V, R>(this: Map<K, V>, functor: (value: V) => R): R[] {
    return Array.from(this.values()).map(functor);
};

Map.prototype.deleteKey = function <K, V>(this: Map<K, V>, key: K): Map<K, V> {
    this.delete(key);
    return new Map<K,V>(this);
};

Map.prototype.addValue = function <K, V>(this: Map<K, V>, key: K, value: V): Map<K, V> {
    this.set(key, value);
    return new Map<K,V>(this);
};

Array.prototype.groupByIgnoreDuplicates = function <K, T>(this: Array<T>, functor: (element: T) => K): Map<K, T> {
    const map = new Map<K,T>();
    this.forEach(item => map.set(functor(item), item));
    return map;
};