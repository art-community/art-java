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

package io.art.entity.immutable;

import io.art.core.lazy.*;
import io.art.entity.constants.*;
import io.art.entity.exception.*;
import io.art.entity.mapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.mapper.ValueToModelMapper.*;
import javax.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@RequiredArgsConstructor
public class ArrayValue implements Value {
    @Getter
    private final ValueType type = ARRAY;
    private final Function<Integer, ? extends Value> valuesProvider;
    private final LazyValue<Integer> size;

    public int size() {
        return size.get();
    }


    public Value get(int index) {
        return valuesProvider.apply(index);
    }

    public <T> T map(int index, ValueToModelMapper<T, ? extends Value> mapper) {
        return let(cast(get(index)), mapper::map);
    }


    public List<Value> toList() {
        List<Value> list = dynamicArrayOf();
        for (int index = 0; index < size(); index++) {
            apply(get(index), list::add);
        }
        return list;
    }

    public Set<Value> toSet() {
        Set<Value> set = setOf();
        for (int index = 0; index < size(); index++) {
            apply(get(index), set::add);
        }
        return set;
    }

    public Queue<Value> toQueue() {
        Queue<Value> queue = queueOf();
        for (int index = 0; index < size(); index++) {
            apply(get(index), queue::add);
        }
        return queue;
    }

    public Deque<Value> toDeque() {
        Deque<Value> deque = dequeOf();
        for (int index = 0; index < size(); index++) {
            apply(get(index), deque::add);
        }
        return deque;
    }


    public <T> List<T> mapToList(ValueToModelMapper<T, ? extends Value> mapper) {
        List<T> list = dynamicArrayOf();
        for (int index = 0; index < size(); index++) {
            apply(map(index, mapper), list::add);
        }
        return list;
    }

    public <T> Set<T> mapToSet(ValueToModelMapper<T, ? extends Value> mapper) {
        Set<T> set = setOf();
        for (int index = 0; index < size(); index++) {
            apply(map(index, mapper), set::add);
        }
        return set;
    }

    public <T> Queue<T> mapToQueue(ValueToModelMapper<T, ? extends Value> mapper) {
        Queue<T> queue = queueOf();
        for (int index = 0; index < size(); index++) {
            apply(map(index, mapper), queue::add);
        }
        return queue;
    }

    public <T> Deque<T> mapToDeque(ValueToModelMapper<T, ? extends Value> mapper) {
        Deque<T> deque = dequeOf();
        for (int index = 0; index < size(); index++) {
            apply(map(index, mapper), deque::add);
        }
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


    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    private class ProxyList<T> implements List<T> {
        private final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<List<T>> evaluated;

        public ProxyList(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.mapToList(mapper));
        }

        private final Iterator<T> iterator = new Iterator<T>() {
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
        };

        private ListIterator<T> createListIterator(int fromIndex) {
            return new ListIterator<T>() {
                private int cursor = fromIndex;

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
                    throw new ValueMethodNotImplementedException("iterator.remove");
                }

                @Override
                public void set(T element) {
                    throw new ValueMethodNotImplementedException("iterator.set");
                }

                @Override
                public void add(T t) {
                    throw new ValueMethodNotImplementedException("iterator.add");
                }
            };
        }

        @Override
        public int size() {
            return ArrayValue.this.size();
        }

        @Override
        public boolean isEmpty() {
            return ArrayValue.this.isEmpty();
        }

        @Override
        public boolean contains(Object value) {
            return evaluated.get().contains(value);
        }

        @Override
        @Nonnull
        public Iterator<T> iterator() {
            return iterator;
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
            throw new ValueMethodNotImplementedException("add");
        }

        @Override
        public boolean remove(Object object) {
            throw new ValueMethodNotImplementedException("remove");
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> collection) {
            return evaluated.get().containsAll(collection);
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends T> collection) {
            throw new ValueMethodNotImplementedException("addAll");
        }

        @Override
        public boolean addAll(int index, @Nonnull Collection<? extends T> collection) {
            throw new ValueMethodNotImplementedException("addAll");
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> collection) {
            throw new ValueMethodNotImplementedException("removeAll");
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new ValueMethodNotImplementedException("retainAll");
        }

        @Override
        public void clear() {
            throw new ValueMethodNotImplementedException("clear");
        }

        @Override
        public T get(int index) {
            return ArrayValue.this.map(index, mapper);
        }

        @Override
        public T set(int index, T element) {
            throw new ValueMethodNotImplementedException("set");
        }

        @Override
        public void add(int index, T element) {
            throw new ValueMethodNotImplementedException("add");
        }

        @Override
        public T remove(int index) {
            throw new ValueMethodNotImplementedException("remove");
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
            return createListIterator(0);
        }

        @Override
        @Nonnull
        public ListIterator<T> listIterator(int index) {
            return createListIterator(index);
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

        private final Iterator<T> iterator = new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < ArrayValue.this.size();
            }

            @Override
            public T next() {
                T value = ArrayValue.this.map(index, mapper);
                index++;
                return value;
            }
        };

        @Override
        public int size() {
            return ArrayValue.this.size();
        }

        @Override
        public boolean isEmpty() {
            return ArrayValue.this.isEmpty();
        }

        @Override
        public boolean contains(Object value) {
            return evaluated.get().contains(value);
        }

        @Override
        @Nonnull
        public Iterator<T> iterator() {
            return iterator;
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
            throw new ValueMethodNotImplementedException("add");
        }

        @Override
        public boolean remove(Object object) {
            throw new ValueMethodNotImplementedException("remove");
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> collection) {
            return evaluated.get().containsAll(collection);
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends T> collection) {
            throw new ValueMethodNotImplementedException("addAll");
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> collection) {
            throw new ValueMethodNotImplementedException("removeAll");
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new ValueMethodNotImplementedException("retainAll");
        }

        @Override
        public void clear() {
            throw new ValueMethodNotImplementedException("clear");
        }
    }

    private class ProxyQueue<T> implements Queue<T> {
        protected final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<Queue<T>> evaluated;
        private int cursor = 0;

        public ProxyQueue(ValueToModelMapper<T, ? extends Value> mapper) {
            this.mapper = mapper;
            this.evaluated = lazy(() -> ArrayValue.this.mapToQueue(mapper));
        }

        private final Iterator<T> iterator = new Iterator<T>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < ArrayValue.this.size();
            }

            @Override
            public T next() {
                T value = ArrayValue.this.map(index, mapper);
                index++;
                return value;
            }
        };

        @Override
        public int size() {
            return ArrayValue.this.size();
        }

        @Override
        public boolean isEmpty() {
            return ArrayValue.this.isEmpty();
        }

        @Override
        public boolean contains(Object value) {
            return evaluated.get().contains(value);
        }

        @Override
        @Nonnull
        public Iterator<T> iterator() {
            return iterator;
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
            throw new ValueMethodNotImplementedException("add");
        }

        @Override
        public boolean offer(T t) {
            throw new ValueMethodNotImplementedException("offer");
        }

        @Override
        public T remove() {
            throw new ValueMethodNotImplementedException("offer");
        }

        @Override
        public T poll() {
            throw new ValueMethodNotImplementedException("offer");
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
            T element = ArrayValue.this.map(cursor, mapper);
            cursor++;
            return element;
        }

        @Override
        public boolean remove(Object object) {
            throw new ValueMethodNotImplementedException("remove");
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> collection) {
            return evaluated.get().containsAll(collection);
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends T> collection) {
            throw new ValueMethodNotImplementedException("addAll");
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> collection) {
            throw new ValueMethodNotImplementedException("removeAll");
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> collection) {
            throw new ValueMethodNotImplementedException("retainAll");
        }

        @Override
        public void clear() {
            throw new ValueMethodNotImplementedException("clear");
        }
    }

    private class ProxyDeque<T> extends ProxyQueue<T> implements Deque<T> {
        public ProxyDeque(ValueToModelMapper<T, ? extends Value> mapper) {
            super(mapper);
        }

        private final ListIterator<T> descendingIterator = new ListIterator<T>() {
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
                throw new ValueMethodNotImplementedException("iterator.remove");
            }

            @Override
            public void set(T element) {
                throw new ValueMethodNotImplementedException("iterator.set");
            }

            @Override
            public void add(T t) {
                throw new ValueMethodNotImplementedException("iterator.add");
            }
        };

        @Override
        public void addFirst(T t) {
            throw new ValueMethodNotImplementedException("addFirst");
        }

        @Override
        public void addLast(T t) {
            throw new ValueMethodNotImplementedException("addLast");
        }

        @Override
        public boolean offerFirst(T t) {
            throw new ValueMethodNotImplementedException("offerFirst");
        }

        @Override
        public boolean offerLast(T t) {
            throw new ValueMethodNotImplementedException("offerLast");
        }

        @Override
        public T removeFirst() {
            throw new ValueMethodNotImplementedException("removeFirst");
        }

        @Override
        public T removeLast() {
            throw new ValueMethodNotImplementedException("removeLast");
        }

        @Override
        public T pollFirst() {
            throw new ValueMethodNotImplementedException("pollFirst");
        }

        @Override
        public T pollLast() {
            throw new ValueMethodNotImplementedException("pollLast");
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
            throw new ValueMethodNotImplementedException("removeFirstOccurrence");
        }

        @Override
        public boolean removeLastOccurrence(Object o) {
            throw new ValueMethodNotImplementedException("removeLastOccurrence");
        }

        @Override
        public void push(T t) {
            throw new ValueMethodNotImplementedException("push");
        }

        @Override
        public T pop() {
            throw new ValueMethodNotImplementedException("pop");
        }

        @Override
        @Nonnull
        public Iterator<T> descendingIterator() {
            return descendingIterator;
        }
    }

    public static ArrayValue EMPTY = new ArrayValue(index -> null, lazy(() -> 0));
}
