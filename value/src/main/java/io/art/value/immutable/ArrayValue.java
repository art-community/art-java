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

package io.art.value.immutable;

import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.core.managed.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.exception.*;
import io.art.value.mapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.QueueFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.managed.LazyValue.*;
import static io.art.value.constants.ValueModuleConstants.ExceptionMessages.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.*;
import static io.art.value.mapper.ValueToModelMapper.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Spliterator.*;
import javax.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@RequiredArgsConstructor
public class ArrayValue implements Value {
    @Getter
    private final ValueType type = ARRAY;
    private final Map<Integer, ?> mappedValueCache = concurrentMap();
    private final Function<Integer, ? extends Value> valueProvider;
    private final LazyValue<Integer> size;

    public int size() {
        return size.get();
    }


    public Value get(int index) {
        return valueProvider.apply(index);
    }

    public <T> T map(int index, ValueToModelMapper<T, ? extends Value> mapper) {
        try {
            Object cached = mappedValueCache.get(index);
            if (nonNull(cached)) return cast(cached);
            cached = let(cast(get(index)), mapper::map);
            if (nonNull(cached)) mappedValueCache.put(index, cast(cached));
            return cast(cached);
        } catch (Throwable throwable) {
            throw new ValueMappingException(format(INDEX_MAPPING_EXCEPTION, index), throwable);
        }
    }


    public List<Value> toList() {
        List<Value> list = dynamicArrayOf();
        for (int index = 0; index < size(); index++) apply(get(index), list::add);
        return list;
    }

    public Set<Value> toSet() {
        Set<Value> set = setOf();
        for (int index = 0; index < size(); index++) apply(get(index), set::add);
        return set;
    }

    public Queue<Value> toQueue() {
        Queue<Value> queue = queueOf();
        for (int index = 0; index < size(); index++) apply(get(index), queue::add);
        return queue;
    }

    public Deque<Value> toDeque() {
        Deque<Value> deque = dequeOf();
        for (int index = 0; index < size(); index++) apply(get(index), deque::add);
        return deque;
    }


    public <T> List<T> toList(ValueToModelMapper<T, ? extends Value> mapper) {
        List<T> list = dynamicArrayOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), list::add);
        return list;
    }

    public <T> Set<T> toSet(ValueToModelMapper<T, ? extends Value> mapper) {
        Set<T> set = setOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), set::add);
        return set;
    }

    public <T> Queue<T> toQueue(ValueToModelMapper<T, ? extends Value> mapper) {
        Queue<T> queue = queueOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), queue::add);
        return queue;
    }

    public <T> Deque<T> toDeque(ValueToModelMapper<T, ? extends Value> mapper) {
        Deque<T> deque = dequeOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), deque::add);
        return deque;
    }


    public List<Value> asList() {
        return asList(identity());
    }

    public Stream<Value> asStream() {
        return asStream(identity());
    }

    public Set<Value> asSet() {
        return asSet(identity());
    }

    public ImmutableArray<Value> asImmutableArray() {
        return new ProxyList<>(identity());
    }

    public ImmutableSet<Value> asImmutableSet() {
        return new ProxySet<>(identity());
    }


    public <T> List<T> asList(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxyList<>(mapper);
    }

    public <T> Stream<T> asStream(ValueToModelMapper<T, ? extends Value> mapper) {
        return asList(mapper).stream();
    }

    public <T> Set<T> asSet(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxySet<>(mapper);
    }

    public <T> ImmutableArray<T> asImmutableArray(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxyList<>(mapper);
    }

    public <T> ImmutableSet<T> asImmutableSet(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxySet<>(mapper);
    }


    public int[] intArray() {
        return unbox(asList(toInt).toArray(new Integer[0]));
    }

    public long[] longArray() {
        return unbox(asList(toLong).toArray(new Long[0]));
    }

    public short[] shortArray() {
        return unbox(asList(toShort).toArray(new Short[0]));
    }

    public double[] doubleArray() {
        return unbox(asList(toDouble).toArray(new Double[0]));
    }

    public float[] floatArray() {
        return unbox(asList(toFloat).toArray(new Float[0]));
    }

    public byte[] byteArray() {
        return unbox(asList(toByte).toArray(new Byte[0]));
    }

    public char[] charArray() {
        return unbox(asList(toChar).toArray(new Character[0]));
    }

    public boolean[] boolArray() {
        return unbox(asList(toBool).toArray(new Boolean[0]));
    }


    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof ArrayValue)) return false;

        ArrayValue another = (ArrayValue) object;

        int size = size();
        if (size != another.size()) return false;

        Value entry;
        for (int index = 0; index < size; index++) {
            entry = this.get(index);
            if (isNull(entry)) {
                if (isNull(another.get(index))) continue;
                return false;
            }
            if (!(entry.equals(another.get(index)))) return false;
        }
        return true;
    }


    private class ProxyList<T> implements List<T>, ImmutableArray<T> {
        private final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<List<T>> evaluated;

        public ProxyList(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.toList(mapper));
        }

        private class ProxyIterator implements Iterator<T> {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor != ArrayValue.this.size();
            }

            @Override
            public T next() {
                T value = ArrayValue.this.map(cursor, mapper);
                cursor++;
                return value;
            }
        }

        @AllArgsConstructor
        private class ProxyListIterator implements ListIterator<T> {
            private int cursor;

            @Override
            public boolean hasNext() {
                return cursor != size.get();
            }

            @Override
            public T next() {
                T value = ArrayValue.this.map(cursor, mapper);
                cursor++;
                return value;
            }

            @Override
            public boolean hasPrevious() {
                return cursor != 0;
            }

            @Override
            public T previous() {
                return ArrayValue.this.map(previousIndex(), mapper);
            }

            @Override
            public int nextIndex() {
                return cursor;
            }

            @Override
            public int previousIndex() {
                return cursor - 1;
            }

            @Override
            public void remove() {
                throw new NotImplementedException("iterator.remove");
            }

            @Override
            public void set(T element) {
                throw new NotImplementedException("iterator.set");
            }

            @Override
            public void add(T t) {
                throw new NotImplementedException("iterator.register");
            }
        }

        @Override
        public int size() {
            return ArrayValue.this.size();
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
            return new ProxyIterator();
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
        public boolean add(T element) {
            throw new NotImplementedException("register");
        }

        @Override
        public boolean remove(Object object) {
            throw new NotImplementedException("remove");
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> collection) {
            return evaluated.get().containsAll(collection);
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends T> collection) {
            throw new NotImplementedException("addAll");
        }

        @Override
        public boolean addAll(int index, @Nonnull Collection<? extends T> collection) {
            throw new NotImplementedException("addAll");
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("removeAll");
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            return false;
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("retainAll");
        }

        @Override
        public void replaceAll(UnaryOperator<T> operator) {
            throw new NotImplementedException("replaceAll");
        }

        @Override
        public void sort(Comparator<? super T> c) {
            throw new NotImplementedException("sort");
        }

        @Override
        public void clear() {
            throw new NotImplementedException("clear");
        }

        @Override
        public T get(int index) {
            return ArrayValue.this.map(index, mapper);
        }

        @Override
        public List<T> toMutable() {
            return evaluated.get();
        }

        @Override
        public T set(int index, T element) {
            throw new NotImplementedException("set");
        }

        @Override
        public void add(int index, T element) {
            throw new NotImplementedException("register");
        }

        @Override
        public T remove(int index) {
            throw new NotImplementedException("remove");
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
        @Nonnull
        public ListIterator<T> listIterator() {
            return new ProxyListIterator(0);
        }

        @Override
        @Nonnull
        public ListIterator<T> listIterator(int index) {
            return new ProxyListIterator(index);
        }

        @Override
        @Nonnull
        public List<T> subList(int fromIndex, int toIndex) {
            return evaluated.get().subList(fromIndex, toIndex);
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
    }

    private class ProxySet<T> implements Set<T>, ImmutableSet<T> {
        private final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<Set<T>> evaluated;

        public ProxySet(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.toSet(mapper));
        }

        private class ProxyIterator implements Iterator<T> {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor != ArrayValue.this.size();
            }

            @Override
            public T next() {
                T value = ArrayValue.this.map(cursor, mapper);
                cursor++;
                return value;
            }
        }

        @Override
        public Set<T> toMutable() {
            return evaluated.get();
        }

        @Override
        public int size() {
            return ArrayValue.this.size();
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
            return new ProxyIterator();
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
        public boolean add(T element) {
            throw new NotImplementedException("register");
        }

        @Override
        public boolean remove(Object object) {
            throw new NotImplementedException("remove");
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> collection) {
            return evaluated.get().containsAll(collection);
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends T> collection) {
            throw new NotImplementedException("addAll");
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("removeAll");
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            throw new NotImplementedException("removeIf");
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("retainAll");
        }

        @Override
        public void clear() {
            throw new NotImplementedException("clear");
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
    }

    public static ArrayValue EMPTY = new ArrayValue(index -> null, lazy(() -> 0));
}
