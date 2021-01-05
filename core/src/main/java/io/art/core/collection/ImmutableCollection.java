package io.art.core.collection;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface ImmutableCollection<T> extends Iterable<T> {
    int size();

    boolean isEmpty();

    boolean contains(Object object);

    boolean containsAll(Collection<?> c);

    Object[] toArray();

    <A> A[] toArray(A[] array);

    Stream<T> stream();

    Stream<T> parallelStream();

    void forEach(Consumer<? super T> action);

    Iterator<T> iterator();

    Spliterator<T> spliterator();
}
