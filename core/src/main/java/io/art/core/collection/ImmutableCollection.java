package io.art.core.collection;

import io.art.core.factory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.SetFactory.immutableSetOf;
import static io.art.core.factory.SetFactory.setOf;
import static java.util.Spliterator.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface ImmutableCollection<T> extends Iterable<T> {
    int size();

    boolean contains(Object object);

    boolean containsAll(Collection<?> collection);

    Object[] toArray();

    <A> A[] toArray(A[] array);

    Iterator<T> iterator();

    default boolean isEmpty() {
        return size() == 0;
    }

    default T[] toArray(Function<Integer, T[]> factory) {
        return toArray(factory.apply(size()));
    }

    default T[] toArrayRaw(Function<Integer, ?> factory) {
        T[] array = cast(factory.apply(size()));
        return toArray(array);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    default ImmutableSet<T> asSet() {
        return immutableSetOf(this);
    }

    default ImmutableArray<T> asArray() {
        return immutableArrayOf(this);
    }

    default Set<T> asMutableSet() {
        return setOf(asSet());
    }

    default List<T> asMutableArray() {
        return dynamicArrayOf(asArray());
    }

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), size(), ORDERED);
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        iterator().forEachRemaining(action);
    }
}
