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

package io.art.meta.transformer;

import io.art.core.collection.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.collector.QueueCollector.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.QueueFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.stream.StreamSupport.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class CollectionTransformers {
    public static MetaTransformer<Collection<?>> collectionTransformer(MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<Collection<?>>() {
            public Collection<?> transform(Iterable<?> value) {
                Collection<?> collection = dynamicArray();
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }

            public Collection<?> transform(byte[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(int[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(short[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(float[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(double[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(long[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(char[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public Collection<?> transform(Stream<?> value) {
                return value.map(parameterTransformer::transform).collect(arrayCollector());
            }

            public Collection<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }

            public Collection<?> transform(ImmutableCollection<?> value) {
                Collection<?> collection = dynamicArray(value.size());
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }

            public Collection<?> transform(Collection<?> value) {
                Collection<?> collection = dynamicArray(value.size());
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }

            public Collection<?> transform(Queue<?> value) {
                Collection<?> collection = queue();
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }

            public Collection<?> transform(Deque<?> value) {
                Collection<?> collection = deque();
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }

            public Collection<?> transform(Set<?> value) {
                Collection<?> collection = set(value.size());
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }

            public Collection<?> transform(ImmutableSet<?> value) {
                Collection<?> collection = set(value.size());
                for (Object element : value) {
                    collection.add(cast(parameterTransformer.transform(element)));
                }
                return collection;
            }
        };
    }

    public static MetaTransformer<Stream<?>> streamTransformer(MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<Stream<?>>() {
            public Stream<?> transform(Iterable<?> value) {
                return stream(value.spliterator(), false).map(parameterTransformer::transform);
            }

            public Stream<?> transform(byte[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(int[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(short[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(float[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(double[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(long[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(char[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public Stream<?> transform(Stream<?> value) {
                return value.map(parameterTransformer::transform);
            }

            public Stream<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }
        };
    }

    public static MetaTransformer<List<?>> listTransformer(MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<List<?>>() {
            public List<?> transform(Iterable<?> value) {
                List<?> list = dynamicArray();
                for (Object element : value) {
                    list.add(cast(parameterTransformer.transform(element)));
                }
                return list;
            }

            public List<?> transform(byte[] value) {
                return transform(box(value));
            }

            public List<?> transform(int[] value) {
                return transform(box(value));
            }

            public List<?> transform(short[] value) {
                return transform(box(value));
            }

            public List<?> transform(float[] value) {
                return transform(box(value));
            }

            public List<?> transform(double[] value) {
                return transform(box(value));
            }

            public List<?> transform(long[] value) {
                return transform(box(value));
            }

            public List<?> transform(char[] value) {
                return transform(box(value));
            }

            public List<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public List<?> transform(Stream<?> value) {
                return value.map(parameterTransformer::transform).collect(arrayCollector());
            }

            public List<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }

            public List<?> transform(ImmutableCollection<?> value) {
                List<?> list = dynamicArray(value.size());
                for (Object element : value) {
                    list.add(cast(parameterTransformer.transform(element)));
                }
                return list;
            }

            public List<?> transform(Collection<?> value) {
                List<?> list = dynamicArray(value.size());
                for (Object element : value) {
                    list.add(cast(parameterTransformer.transform(element)));
                }
                return list;
            }
        };
    }

    public static MetaTransformer<Set<?>> setTransformer(MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<Set<?>>() {
            public Set<?> transform(Iterable<?> value) {
                Set<?> set = set();
                for (Object element : value) {
                    set.add(cast(parameterTransformer.transform(element)));
                }
                return set;
            }

            public Set<?> transform(byte[] value) {
                return transform(box(value));
            }

            public Set<?> transform(int[] value) {
                return transform(box(value));
            }

            public Set<?> transform(short[] value) {
                return transform(box(value));
            }

            public Set<?> transform(float[] value) {
                return transform(box(value));
            }

            public Set<?> transform(double[] value) {
                return transform(box(value));
            }

            public Set<?> transform(long[] value) {
                return transform(box(value));
            }

            public Set<?> transform(char[] value) {
                return transform(box(value));
            }

            public Set<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public Set<?> transform(Stream<?> value) {
                return value.map(parameterTransformer::transform).collect(setCollector());
            }

            public Set<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }

            public Set<?> transform(ImmutableCollection<?> value) {
                Set<?> set = set(value.size());
                for (Object element : value) {
                    set.add(cast(parameterTransformer.transform(element)));
                }
                return set;
            }

            public Set<?> transform(Collection<?> value) {
                Set<?> set = set(value.size());
                for (Object element : value) {
                    set.add(cast(parameterTransformer.transform(element)));
                }
                return set;
            }
        };
    }

    public static MetaTransformer<Queue<?>> queueTransformer(MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<Queue<?>>() {
            public Queue<?> transform(Iterable<?> value) {
                Queue<?> queue = queue();
                for (Object element : value) {
                    queue.add(cast(parameterTransformer.transform(element)));
                }
                return queue;
            }

            public Queue<?> transform(byte[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(int[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(short[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(float[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(double[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(long[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(char[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public Queue<?> transform(Stream<?> value) {
                return value.map(parameterTransformer::transform).collect(queueCollector());
            }

            public Queue<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }

            public Queue<?> transform(ImmutableCollection<?> value) {
                Queue<?> queue = queue();
                for (Object element : value) {
                    queue.add(cast(parameterTransformer.transform(element)));
                }
                return queue;
            }

            public Queue<?> transform(Collection<?> value) {
                Queue<?> queue = queue();
                for (Object element : value) {
                    queue.add(cast(parameterTransformer.transform(element)));
                }
                return queue;
            }

        };
    }

    public static MetaTransformer<Deque<?>> dequeTransformer(MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<Deque<?>>() {
            public Deque<?> transform(Iterable<?> value) {
                Deque<?> deque = deque();
                for (Object element : value) {
                    deque.add(cast(parameterTransformer.transform(element)));
                }
                return deque;
            }

            public Deque<?> transform(byte[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(int[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(short[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(float[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(double[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(long[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(char[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(boolean[] value) {
                return transform(box(value));
            }

            public Deque<?> transform(Stream<?> value) {
                return value.map(parameterTransformer::transform).collect(dequeCollector());
            }

            public Deque<?> transform(Flux<?> value) {
                return transform(value.toStream());
            }

            public Deque<?> transform(ImmutableCollection<?> value) {
                Deque<?> deque = deque();
                for (Object element : value) {
                    deque.add(cast(parameterTransformer.transform(element)));
                }
                return deque;
            }

            public Deque<?> transform(Collection<?> value) {
                Deque<?> deque = deque();
                for (Object element : value) {
                    deque.add(cast(parameterTransformer.transform(element)));
                }
                return deque;
            }

        };
    }
}
