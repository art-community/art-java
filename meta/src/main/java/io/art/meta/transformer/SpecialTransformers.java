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

import io.art.core.extensions.*;
import io.art.core.property.*;
import io.art.core.stream.*;
import io.art.meta.exception.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.NettyBufferExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.util.Optional.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class SpecialTransformers {
    public static MetaTransformer<ByteBuf> NETTY_BUFFER_TRANSFORMER = new MetaTransformer<ByteBuf>() {
        public ByteBuf transform(ByteBuf value) {
            return value;
        }

        public ByteBuf transform(byte[] value) {
            return from(value);
        }

        public ByteBuf transform(String value) {
            return from(value);
        }

        public ByteBuf transform(InputStream value) {
            return from(value);
        }

        public ByteBuf transform(Path value) {
            return from(readFileBytesQuietly(value));
        }

        public ByteBuf transform(File value) {
            return from(readFileBytesQuietly(value.toPath()));
        }

        public ByteBuf transform(ByteBuffer value) {
            return from(value);
        }
    };

    public static MetaTransformer<ByteBuffer> NIO_BUFFER_TRANSFORMER = new MetaTransformer<ByteBuffer>() {
        public ByteBuffer transform(ByteBuffer value) {
            return value;
        }

        public ByteBuffer transform(byte[] value) {
            return NioBufferExtensions.from(value);
        }

        public ByteBuffer transform(String value) {
            return NioBufferExtensions.from(value);
        }

        public ByteBuffer transform(InputStream value) {
            return NioBufferExtensions.from(value);
        }

        public ByteBuffer transform(Path value) {
            return NioBufferExtensions.from(readFileBytesQuietly(value));
        }

        public ByteBuffer transform(File value) {
            return NioBufferExtensions.from(readFileBytesQuietly(value.toPath()));
        }

        public ByteBuffer transform(ByteBuf value) {
            return NioBufferExtensions.from(value);
        }
    };

    public static MetaTransformer<InputStream> INPUT_STREAM_TRANSFORMER = new MetaTransformer<InputStream>() {
        public InputStream transform(InputStream value) {
            return value;
        }

        public InputStream transform(byte[] value) {
            return new ByteArrayInputStream(value);
        }

        public InputStream transform(String value) {
            return new ByteArrayInputStream(value.getBytes(context().configuration().getCharset()));
        }

        public InputStream transform(Path value) {
            return fileInputStream(value);
        }

        public InputStream transform(File value) {
            return fileInputStream(value);
        }

        public InputStream transform(ByteBuf value) {
            return new ByteBufInputStream(value);
        }

        public InputStream transform(ByteBuffer value) {
            return new NioByteBufferInputStream(value);
        }
    };

    public static MetaTransformer<OutputStream> OUTPUT_STREAM_TRANSFORMER = new MetaTransformer<OutputStream>() {
        public OutputStream transform(OutputStream value) {
            return value;
        }

        public OutputStream transform(byte[] value) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(value.length);
            ignoreException(() -> outputStream.write(value), TransformationException::new);
            return outputStream;
        }

        public OutputStream transform(String value) {
            return transform(value.getBytes(context().configuration().getCharset()));
        }

        public OutputStream transform(Path value) {
            return fileOutputStream(value);
        }

        public OutputStream transform(File value) {
            return fileOutputStream(value);
        }

        public OutputStream transform(ByteBuf value) {
            return new ByteBufOutputStream(value);
        }

        public OutputStream transform(ByteBuffer value) {
            return new NioByteBufferOutputStream(value);
        }
    };

    public static MetaTransformer<LazyProperty<?>> lazyTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<LazyProperty<?>>() {
            @Override
            public LazyProperty<?> transform(Object value) {
                return lazy(() -> parameterTransformer.transform(value));
            }

            public LazyProperty<?> transform(LazyProperty<?> value) {
                return lazy(() -> parameterTransformer.transform(value.get()));
            }
        };
    }

    public static MetaTransformer<Supplier<?>> supplierTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<Supplier<?>>() {
            @Override
            public Supplier<?> transform(Object value) {
                return () -> parameterTransformer.transform(value);
            }

            public Supplier<?> transform(Supplier<?> value) {
                return () -> parameterTransformer.transform(value.get());
            }
        };
    }

    public static MetaTransformer<Optional<?>> optionalTransformer(MetaTransformer<?> parameterTransformer) {
        return new MetaTransformer<Optional<?>>() {
            @Override
            public Optional<?> transform(Object value) {
                return ofNullable(parameterTransformer.transform(value));
            }

            public Optional<?> transform(Optional<?> value) {
                return value.map(parameterTransformer::transform);
            }
        };
    }

    public static <E extends Enum<?>> MetaTransformer<E> enumTransformer(Function<String, E> enumFactory) {
        return new MetaTransformer<E>() {
            public E transform(E value) {
                return value;
            }

            public E transform(String value) {
                return enumFactory.apply(value);
            }
        };
    }

    public static MetaTransformer<Void> VOID_TRANSFORMER = new MetaTransformer<>() {
        @Override
        public Void transform(Object value) {
            return null;
        }

        @Override
        public Void transform(Optional<?> value) {
            return null;
        }

        @Override
        public Void transform(Mono<?> value) {
            return null;
        }

        @Override
        public Void transform(Supplier<?> value) {
            return null;
        }
    };
}
