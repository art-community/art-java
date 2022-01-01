/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import java.util.*;

public class ImmutableSetImplementation<T> implements ImmutableSet<T> {
    private final Set<T> set;

    public ImmutableSetImplementation(Iterable<T> collection) {
        this.set = com.google.common.collect.ImmutableSet.copyOf(collection);
    }

    private ImmutableSetImplementation(com.google.common.collect.ImmutableSet<T> set) {
        this.set = set;
    }

    @Override
    public Set<T> toMutable() {
        return new LinkedHashSet<>(set);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object object) {
        return set.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return set.containsAll(collection);
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <A> A[] toArray(A[] array) {
        return set.toArray(array);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof ImmutableSetImplementation)) return false;
        return set.equals(((ImmutableSetImplementation<?>) object).set);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
