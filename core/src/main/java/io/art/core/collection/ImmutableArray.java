/*
 * ART
 *
 * Copyright 2020 ART
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

import com.google.common.collect.*;
import static io.art.core.caster.Caster.*;
import static java.util.Collections.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ImmutableArray<T> implements Iterable<T> {
    private final List<T> array;

    private static final ImmutableArray<?> EMPTY = new ImmutableArray<>(emptyList());

    private static final Collector<Object, ?, ImmutableArray<Object>> COLLECTOR = Collector.of(
            ImmutableArray::immutableArrayBuilder,
            Builder::add,
            Builder::combine,
            Builder::build
    );

    public ImmutableArray(Collection<T> collection) {
        this.array = new ArrayList<>(collection);
    }

    public ImmutableArray(ImmutableList<T> list) {
        this.array = list;
    }

    public int size() {
        return array.size();
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    public boolean contains(Object object) {
        return array.contains(object);
    }

    public boolean containsAll(Collection<?> c) {
        return array.containsAll(c);
    }

    public Object[] toArray() {
        return array.toArray();
    }

    public T[] toArray(T[] array) {
        return this.array.toArray(array);
    }

    public T get(int index) {
        return array.get(index);
    }

    public Stream<T> stream() {
        return array.stream();
    }

    public Stream<T> parallelStream() {
        return array.parallelStream();
    }

    public void forEach(Consumer<? super T> action) {
        array.forEach(action);
    }

    public List<T> toMutable() {
        return new ArrayList<>(array);
    }

    @Override
    public Iterator<T> iterator() {
        return array.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return array.spliterator();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof ImmutableArray)) return false;
        return array.equals(((ImmutableArray<?>) object).array);
    }

    @Override
    public int hashCode() {
        return array.hashCode();
    }

    public int indexOf(Object object) {
        return array.indexOf(object);
    }

    public int lastIndexOf(Object object) {
        return array.lastIndexOf(object);
    }

    public static <T> ImmutableArray<T> emptyImmutableArray() {
        return cast(EMPTY);
    }

    public static <T> ImmutableArray<T> immutableSortedArray(Collection<T> elements, Comparator<T> comparator) {
        return new ImmutableArray<>(Ordering.from(comparator).immutableSortedCopy(elements));
    }

    public static <T> Collector<T, T, ImmutableArray<T>> immutableArrayCollector() {
        return cast(COLLECTOR);
    }

    public static <T> Builder<T> immutableArrayBuilder() {
        return new Builder<>();
    }

    public static <T> Builder<T> immutableArrayBuilder(int size) {
        return new Builder<>(size);
    }

    public static class Builder<T> {
        private final ImmutableList.Builder<T> builder;

        public Builder() {
            builder = ImmutableList.builder();
        }

        public Builder(int size) {
            builder = ImmutableList.builderWithExpectedSize(size);
        }

        public Builder<T> add(T element) {
            builder.add(element);
            return this;
        }

        @SafeVarargs
        public final Builder<T> add(T... elements) {
            builder.add(elements);
            return this;
        }

        public Builder<T> addAll(Iterable<? extends T> elements) {
            builder.addAll(elements);
            return this;
        }

        public Builder<T> addAll(Iterator<? extends T> elements) {
            builder.addAll(elements);
            return this;
        }

        private Builder<T> combine(Builder<T> builder) {
            this.builder.addAll(builder.build());
            return this;
        }

        public ImmutableArray<T> build() {
            return new ImmutableArray<>(builder.build());
        }
    }
}
