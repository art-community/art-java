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

import static io.art.core.caster.Caster.*;
import static java.util.Collections.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ImmutableSet<T> implements Iterable<T> {
    private final Set<T> set;

    private static final ImmutableSet<?> EMPTY = new ImmutableSet<>(emptySet());

    private static final Collector<Object, ?, ImmutableSet<Object>> COLLECTOR = Collector.of(
            ImmutableSet::builder,
            Builder::add,
            Builder::combine,
            Builder::build
    );

    public ImmutableSet(Collection<T> collection) {
        this.set = com.google.common.collect.ImmutableSet.copyOf(collection);
    }

    private ImmutableSet(com.google.common.collect.ImmutableSet<T> set) {
        this.set = set;
    }

    public int size() {
        return set.size();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public boolean contains(Object object) {
        return set.contains(object);
    }

    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    public Object[] toArray() {
        return set.toArray();
    }

    public T[] toArray(T[] array) {
        return this.set.toArray(array);
    }

    public Stream<T> stream() {
        return set.stream();
    }

    public Stream<T> parallelStream() {
        return set.parallelStream();
    }

    public void forEach(Consumer<? super T> action) {
        set.forEach(action);
    }

    public Set<T> toMutable() {
        return new LinkedHashSet<>(set);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return set.spliterator();
    }

    @Override
    public boolean equals(Object object) {
        return set.equals(object);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    public static <T> ImmutableSet<T> empty() {
        return cast(EMPTY);
    }

    public static <T> Collector<T, T, ImmutableSet<T>> collector() {
        return cast(COLLECTOR);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final com.google.common.collect.ImmutableSet.Builder<T> builder = com.google.common.collect.ImmutableSet.builder();

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

        public ImmutableSet<T> build() {
            return new ImmutableSet<>(builder.build());
        }
    }
}
