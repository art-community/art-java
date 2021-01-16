package io.art.core.collection;

import static io.art.core.caster.Caster.*;
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

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), size(), ORDERED);
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        iterator().forEachRemaining(action);
    }
}
