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

import io.art.core.exception.*;
import io.art.core.lazy.*;
import io.art.value.constants.ValueConstants.*;
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
import static io.art.core.lazy.LazyValue.*;
import static io.art.value.constants.ValueConstants.ExceptionMessages.FIELD_MAPPING_EXCEPTION;
import static io.art.value.constants.ValueConstants.ExceptionMessages.INDEX_MAPPING_EXCEPTION;
import static io.art.value.constants.ValueConstants.ValueType.*;
import static io.art.value.mapper.ValueToModelMapper.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import static java.text.MessageFormat.format;
import javax.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@RequiredArgsConstructor
public class ArrayValue implements Value {
    @Getter
    private final ValueType type = ARRAY;
    private final Map<Integer, LazyValue<?>> mappedValueCache = concurrentHashMap();
    private final Function<Integer, ? extends Value> valueProvider;
    private final LazyValue<Integer> size;

    public int size() {
        return size.get();
    }


    public Value get(int index) {
        return valueProvider.apply(index);
    }

    public <T> T map(int index, ValueToModelMapper<T, ? extends Value> mapper) {
        LazyValue<?> lazyValue = mappedValueCache.computeIfAbsent(index, key -> lazy(() -> cast(get(key))));
        Object result = let(lazyValue, LazyValue::get);
        try {
            return mapper.map(cast(result));
        } catch (Throwable throwable) {
            throw new ValueMappingException(format(INDEX_MAPPING_EXCEPTION, index));
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


    public <T> List<T> mapToList(ValueToModelMapper<T, ? extends Value> mapper) {
        List<T> list = dynamicArrayOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), list::add);
        return list;
    }

    public <T> Set<T> mapToSet(ValueToModelMapper<T, ? extends Value> mapper) {
        Set<T> set = setOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), set::add);
        return set;
    }

    public <T> Queue<T> mapToQueue(ValueToModelMapper<T, ? extends Value> mapper) {
        Queue<T> queue = queueOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), queue::add);
        return queue;
    }

    public <T> Deque<T> mapToDeque(ValueToModelMapper<T, ? extends Value> mapper) {
        Deque<T> deque = dequeOf();
        for (int index = 0; index < size(); index++) apply(map(index, mapper), deque::add);
        return deque;
    }


    public List<Value> asList() {
        return mapAsList(identity());
    }

    public Stream<Value> asStream() {
        return mapAsStream(identity());
    }

    public Set<Value> asSet() {
        return mapAsSet(identity());
    }

    public Queue<Value> asQueue() {
        return mapAsQueue(identity());
    }

    public Deque<Value> asDeque() {
        return mapAsDeque(identity());
    }


    public <T> List<T> mapAsList(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxyList<>(mapper);
    }

    public <T> Stream<T> mapAsStream(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapAsList(mapper).stream();
    }

    public <T> Set<T> mapAsSet(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxySet<>(mapper);
    }

    public <T> Queue<T> mapAsQueue(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxyQueue<>(mapper);
    }

    public <T> Deque<T> mapAsDeque(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxyDeque<>(mapper);
    }


    public int[] intArray() {
        return unbox(mapAsList(toInt).toArray(new Integer[0]));
    }

    public long[] longArray() {
        return unbox(mapAsList(toLong).toArray(new Long[0]));
    }

    public short[] shortArray() {
        return unbox(mapAsList(toShort).toArray(new Short[0]));
    }

    public double[] doubleArray() {
        return unbox(mapAsList(toDouble).toArray(new Double[0]));
    }

    public float[] floatArray() {
        return unbox(mapAsList(toFloat).toArray(new Float[0]));
    }

    public byte[] byteArray() {
        return unbox(mapAsList(toByte).toArray(new Byte[0]));
    }

    public char[] charArray() {
        return unbox(mapAsList(toChar).toArray(new Character[0]));
    }

    public boolean[] boolArray() {
        return unbox(mapAsList(toBool).toArray(new Boolean[0]));
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
            if (entry == null) {
                if (another.get(index) == null) continue;
                return false;
            }
            if (!(entry.equals(another.get(index)))) return false;
        }
        return true;
    }


    private class ProxyList<T> implements List<T> {
        private final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<List<T>> evaluated;

        public ProxyList(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.mapToList(mapper));
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
                throw new NotImplementedException("iterator.add");
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
            throw new NotImplementedException("add");
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
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("retainAll");
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
        public T set(int index, T element) {
            throw new NotImplementedException("set");
        }

        @Override
        public void add(int index, T element) {
            throw new NotImplementedException("add");
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
    }

    private class ProxySet<T> implements Set<T> {
        private final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<Set<T>> evaluated;

        public ProxySet(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.mapToSet(mapper));
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
            throw new NotImplementedException("add");
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
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("retainAll");
        }

        @Override
        public void clear() {
            throw new NotImplementedException("clear");
        }
    }

    private class ProxyQueue<T> implements Queue<T> {
        protected final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<Queue<T>> evaluated;
        private int index = 0;

        public ProxyQueue(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.mapToQueue(mapper));
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
            throw new NotImplementedException("add");
        }

        @Override
        public boolean offer(T t) {
            throw new NotImplementedException("offer");
        }

        @Override
        public T remove() {
            throw new NotImplementedException("offer");
        }

        @Override
        public T poll() {
            throw new NotImplementedException("offer");
        }

        @Override
        public T element() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            return peek();
        }

        @Override
        public T peek() {
            T value = ArrayValue.this.map(index, mapper);
            index++;
            return value;
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
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new NotImplementedException("retainAll");
        }

        @Override
        public void clear() {
            throw new NotImplementedException("clear");
        }
    }

    private class ProxyDeque<T> extends ProxyQueue<T> implements Deque<T> {
        public ProxyDeque(ValueToModelMapper<T, ? extends Value> mapper) {
            super(mapper);
        }

        private class ProxyDescendingIterator implements ListIterator<T> {
            private final int index = size.get();

            @Override
            public boolean hasNext() {
                return hasPrevious();
            }

            @Override
            public T next() {
                return previous();
            }

            @Override
            public boolean hasPrevious() {
                return index != 0;
            }

            @Override
            public T previous() {
                return ArrayValue.this.map(previousIndex(), mapper);
            }

            @Override
            public int nextIndex() {
                return index;
            }

            @Override
            public int previousIndex() {
                return index - 1;
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
                throw new NotImplementedException("iterator.add");
            }
        }

        ;

        @Override
        public void addFirst(T t) {
            throw new NotImplementedException("addFirst");
        }

        @Override
        public void addLast(T t) {
            throw new NotImplementedException("addLast");
        }

        @Override
        public boolean offerFirst(T t) {
            throw new NotImplementedException("offerFirst");
        }

        @Override
        public boolean offerLast(T t) {
            throw new NotImplementedException("offerLast");
        }

        @Override
        public T removeFirst() {
            throw new NotImplementedException("removeFirst");
        }

        @Override
        public T removeLast() {
            throw new NotImplementedException("removeLast");
        }

        @Override
        public T pollFirst() {
            throw new NotImplementedException("pollFirst");
        }

        @Override
        public T pollLast() {
            throw new NotImplementedException("pollLast");
        }

        @Override
        public T getFirst() {
            return ArrayValue.this.map(0, mapper);
        }

        @Override
        public T getLast() {
            return ArrayValue.this.map(size.get() - 1, mapper);
        }

        @Override
        public T peekFirst() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            return getFirst();
        }

        @Override
        public T peekLast() {
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            return getLast();
        }

        @Override
        public boolean removeFirstOccurrence(Object o) {
            throw new NotImplementedException("removeFirstOccurrence");
        }

        @Override
        public boolean removeLastOccurrence(Object o) {
            throw new NotImplementedException("removeLastOccurrence");
        }

        @Override
        public void push(T t) {
            throw new NotImplementedException("push");
        }

        @Override
        public T pop() {
            throw new NotImplementedException("pop");
        }

        @Override
        @Nonnull
        public Iterator<T> descendingIterator() {
            return new ProxyDescendingIterator();
        }

    }

    public static ArrayValue EMPTY = new ArrayValue(index -> null, lazy(() -> 0));
}
