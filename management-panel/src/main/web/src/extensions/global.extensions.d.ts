declare global {
    interface Set<T> {
        mapToArray<R>(this: Set<T>, functor: (element: T) => R): R[];
        addValue<T>(this: Set<T>, value: T): Set<T>;
        deleteValue<T>(this: Set<T>,  value: T): Set<T>;
    }

    interface Map<K, V> {
        mapValuesToArray<R>(this: Map<K, V>, functor: (element: V) => R): R[];
        deleteKey(this: Map<K, V>, key: K): Map<K, V>;
        addValue(this: Map<K, V>, key: K, value: V): Map<K, V>;
    }

    interface Array<T> {
        groupByIgnoreDuplicates<K>(this: Array<T>, functor: (element: T) => K): Map<K, T>;
    }
}
export {}