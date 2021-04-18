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

import com.google.common.collect.*;
import javax.annotation.*;
import java.util.*;

public class ImmutableArrayImplementation<T> implements ImmutableArray<T> {
    private final List<T> array;

    public ImmutableArrayImplementation(Iterable<T> collection) {
        this.array = ImmutableList.copyOf(collection);
    }

    public ImmutableArrayImplementation(ImmutableList<T> list) {
        this.array = list;
    }

    @Override
    public T get(int index) {
        return array.get(index);
    }

    @Override
    public List<T> toMutable() {
        return new ArrayList<>(array);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean contains(Object object) {
        return array.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return array.containsAll(collection);
    }

    @Override
    public Object[] toArray() {
        return array.toArray();
    }

    @Override
    public <A> A[] toArray(A[] array) {
        return this.array.toArray(array);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return array.iterator();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof ImmutableArrayImplementation)) return false;
        return array.equals(((ImmutableArrayImplementation<?>) object).array);
    }

    @Override
    public int hashCode() {
        return array.hashCode();
    }

    @Override
    public int indexOf(Object object) {
        return array.indexOf(object);
    }

    @Override
    public int lastIndexOf(Object object) {
        return array.lastIndexOf(object);
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
