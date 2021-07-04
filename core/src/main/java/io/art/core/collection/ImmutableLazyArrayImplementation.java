/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.core.collection;

import io.art.core.factory.*;
import io.art.core.property.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Objects.*;
import static java.util.Spliterator.*;
import javax.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ImmutableLazyArrayImplementation<T> implements ImmutableArray<T> {
    private final IntFunction<T> provider;
    private final int size;
    private final LazyProperty<List<T>> evaluated;

    private final static ImmutableLazyArrayImplementation<?> EMPTY = new ImmutableLazyArrayImplementation<>(index -> null, 0);

    public ImmutableLazyArrayImplementation(IntFunction<T> provider, int size) {
        this.provider = provider;
        this.size = size;
        this.evaluated = lazy(this::collect);
    }

    private class ImmutableLazyArrayIterator implements Iterator<T> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            T value = provider.apply(cursor);
            cursor++;
            return value;
        }
    }

    private List<T> collect() {
        List<T> array = ArrayFactory.dynamicArray(size);
        for (int index = 0; index < size; index++) {
            array.add(provider.apply(index));
        }
        return array;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object value) {
        return evaluated.get().contains(value);
    }

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return new ImmutableLazyArrayIterator();
    }

    @Override
    @Nonnull
    public Object[] toArray() {
        return evaluated.get().toArray();
    }

    @Override
    @Nonnull
    public <A> A[] toArray(@Nonnull A[] array) {
        return evaluated.get().toArray(array);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> collection) {
        return evaluated.get().containsAll(collection);
    }

    @Override
    public T get(int index) {
        return provider.apply(index);
    }

    @Override
    public List<T> toMutable() {
        return evaluated.get();
    }

    @Override
    public int indexOf(Object object) {
        return evaluated.get().indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return evaluated.get().lastIndexOf(object);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(iterator(), size(), ORDERED);
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public int hashCode() {
        return collect().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (isNull(other)) return false;
        if (!(other instanceof ImmutableArray)) return false;
        ImmutableArray<?> otherArray = (ImmutableArray<?>) other;
        if (size != otherArray.size()) return false;
        for (int index = 0; index < size; index++) {
            if (!Objects.deepEquals(provider.apply(index), otherArray.get(index))) {
                return false;
            }
        }
        return true;
    }

    public static <T> ImmutableLazyArrayImplementation<T> emptyImmutableLazyArray() {
        return cast(EMPTY);
    }
}
