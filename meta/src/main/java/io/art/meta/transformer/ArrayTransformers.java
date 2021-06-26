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
import io.art.core.extensions.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ArrayExtensions.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@UtilityClass
public class ArrayTransformers {
    public static MetaTransformer<byte[]> BYTE_ARRAY_TRANSFORMER = new MetaTransformer<byte[]>() {
        public byte[] transform(byte[] value) {
            return value;
        }

        public byte[] transform(Byte[] value) {
            return unbox(value);
        }

        public byte[] transform(Collection<Byte> value) {
            return unbox(value.toArray(new Byte[0]));
        }

        public byte[] transform(ImmutableCollection<Byte> value) {
            return unbox(value.toArray(new Byte[0]));
        }

        public byte[] transform(Stream<Byte> value) {
            return unbox(value.toArray(Byte[]::new));
        }

        public byte[] transform(Flux<Byte> value) {
            return transform(value.toStream());
        }

        public byte[] transform(String value) {
            return value.getBytes(context().configuration().getCharset());
        }

        public byte[] transform(InputStream value) {
            return InputStreamExtensions.toByteArray(value);
        }

        public byte[] transform(Path value) {
            return readFileBytesQuietly(value);
        }

        public byte[] transform(File value) {
            return readFileBytesQuietly(value.toPath());
        }

        public byte[] transform(ByteBuf value) {
            return NettyBufferExtensions.toByteArray(value);
        }

        public byte[] transform(ByteBuffer value) {
            return NioBufferExtensions.toByteArray(value);
        }
    };

    public static MetaTransformer<int[]> INTEGER_ARRAY_TRANSFORMER = new MetaTransformer<int[]>() {
        public int[] transform(int[] value) {
            return value;
        }

        public int[] transform(Collection<Integer> value) {
            return unbox(value.toArray(new Integer[0]));
        }

        public int[] transform(Integer[] value) {
            return unbox(value);
        }

        public int[] transform(ImmutableCollection<Integer> value) {
            return unbox(value.toArray(new Integer[0]));
        }

        public int[] transform(Stream<Integer> value) {
            return unbox(value.toArray(Integer[]::new));
        }

        public int[] transform(Flux<Integer> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<short[]> SHORT_ARRAY_TRANSFORMER = new MetaTransformer<short[]>() {
        public short[] transform(short[] value) {
            return value;
        }

        public short[] transform(Collection<Short> value) {
            return unbox(value.toArray(new Short[0]));
        }

        public short[] transform(Short[] value) {
            return unbox(value);
        }

        public short[] transform(ImmutableCollection<Short> value) {
            return unbox(value.toArray(new Short[0]));
        }

        public short[] transform(Stream<Short> value) {
            return unbox(value.toArray(Short[]::new));
        }

        public short[] transform(Flux<Short> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<float[]> FLOAT_ARRAY_TRANSFORMER = new MetaTransformer<float[]>() {
        public float[] transform(float[] value) {
            return value;
        }

        public float[] transform(Collection<Float> value) {
            return unbox(value.toArray(new Float[0]));
        }

        public float[] transform(Float[] value) {
            return unbox(value);
        }

        public float[] transform(ImmutableCollection<Float> value) {
            return unbox(value.toArray(new Float[0]));
        }

        public float[] transform(Stream<Float> value) {
            return unbox(value.toArray(Float[]::new));
        }

        public float[] transform(Flux<Float> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<double[]> DOUBLE_ARRAY_TRANSFORMER = new MetaTransformer<double[]>() {
        public double[] transform(double[] value) {
            return value;
        }

        public double[] transform(Double[] value) {
            return unbox(value);
        }

        public double[] transform(Collection<Double> value) {
            return unbox(value.toArray(new Double[0]));
        }

        public double[] transform(ImmutableCollection<Double> value) {
            return unbox(value.toArray(new Double[0]));
        }

        public double[] transform(Stream<Double> value) {
            return unbox(value.toArray(Double[]::new));
        }

        public double[] transform(Flux<Double> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<long[]> LONG_ARRAY_TRANSFORMER = new MetaTransformer<long[]>() {
        public long[] transform(long[] value) {
            return value;
        }

        public long[] transform(Collection<Long> value) {
            return unbox(value.toArray(new Long[0]));
        }

        public long[] transform(Long[] value) {
            return unbox(value);
        }

        public long[] transform(ImmutableCollection<Long> value) {
            return unbox(value.toArray(new Long[0]));
        }

        public long[] transform(Stream<Long> value) {
            return unbox(value.toArray(Long[]::new));
        }

        public long[] transform(Flux<Long> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<char[]> CHARACTER_ARRAY_TRANSFORMER = new MetaTransformer<char[]>() {
        public char[] transform(char[] value) {
            return value;
        }

        public char[] transform(Character[] value) {
            return unbox(value);
        }

        public char[] transform(Collection<Character> value) {
            return unbox(value.toArray(new Character[0]));
        }

        public char[] transform(ImmutableCollection<Character> value) {
            return unbox(value.toArray(new Character[0]));
        }

        public char[] transform(Stream<Character> value) {
            return unbox(value.toArray(Character[]::new));
        }

        public char[] transform(Flux<Character> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<boolean[]> BOOLEAN_ARRAY_TRANSFORMER = new MetaTransformer<boolean[]>() {
        public boolean[] transform(boolean[] value) {
            return value;
        }

        public boolean[] transform(Boolean[] value) {
            return unbox(value);
        }

        public boolean[] transform(Collection<Boolean> value) {
            return unbox(value.toArray(new Boolean[0]));
        }

        public boolean[] transform(ImmutableCollection<Boolean> value) {
            return unbox(value.toArray(new Boolean[0]));
        }

        public boolean[] transform(Stream<Boolean> value) {
            return unbox(value.toArray(Boolean[]::new));
        }

        public boolean[] transform(Flux<Boolean> value) {
            return transform(value.toStream());
        }
    };

    public static MetaTransformer<Object[]> arrayTransformer(Function<Integer, Object[]> arrayFactory, MetaTransformer<Object> parameterTransformer) {
        return new MetaTransformer<Object[]>() {
            public Object[] transform(Object[] value) {
                return transform(fixedArrayOf(value));
            }

            public Object[] transform(byte[] value) {
                return transform(box(value));
            }

            public Object[] transform(int[] value) {
                return transform(box(value));
            }

            public Object[] transform(long[] value) {
                return transform(box(value));
            }

            public Object[] transform(float[] value) {
                return transform(box(value));
            }

            public Object[] transform(double[] value) {
                return transform(box(value));
            }

            public Object[] transform(char[] value) {
                return transform(box(value));
            }

            public Object[] transform(boolean[] value) {
                return transform(box(value));
            }

            public Object[] transform(Collection<?> value) {
                Object[] array = arrayFactory.apply(value.size());
                int index = 0;
                for (Object element : value) {
                    array[index++] = parameterTransformer.transform(element);
                }
                return array;
            }

            public Object[] transform(ImmutableCollection<?> value) {
                Object[] array = arrayFactory.apply(value.size());
                int index = 0;
                for (Object element : value) {
                    array[index++] = parameterTransformer.transform(element);
                }
                return array;
            }

            public Object[] transform(Stream<?> value) {
                return value.map(parameterTransformer::transform).toArray(arrayFactory::apply);
            }

            public Object[] transform(Flux<?> value) {
                return transform(value.toStream());
            }
        };
    }
}
