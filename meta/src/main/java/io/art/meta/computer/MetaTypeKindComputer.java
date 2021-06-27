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

package io.art.meta.computer;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.meta.constants.MetaConstants.*;
import io.art.meta.model.*;
import io.art.meta.registry.*;
import io.netty.buffer.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static lombok.AccessLevel.*;
import java.io.*;
import java.nio.*;
import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class MetaTypeKindComputer {
    @Getter(lazy = true, value = PRIVATE)
    private final static ImmutableMap<Class<?>, MetaClass<?>> classes = MetaClassRegistry.classes();

    public static MetaTypeInternalKind computeKind(Class<?> type) {
        if (getClasses().containsKey(type)) return ENTITY;
        if (type.isEnum()) return ENUM;
        if (long[].class.equals(type)) return LONG_ARRAY;
        if (int[].class.equals(type)) return INTEGER_ARRAY;
        if (short[].class.equals(type)) return SHORT_ARRAY;
        if (byte[].class.equals(type)) return BYTE_ARRAY;
        if (double[].class.equals(type)) return DOUBLE_ARRAY;
        if (float[].class.equals(type)) return FLOAT_ARRAY;
        if (char[].class.equals(type)) return CHARACTER_ARRAY;
        if (boolean[].class.equals(type)) return BOOLEAN_ARRAY;
        if (type.isArray()) return ARRAY;
        if (Void.class.equals(type) || void.class.equals(type)) return VOID;
        if (Long.class.equals(type) || long.class.equals(type)) return LONG;
        if (Double.class.equals(type) || double.class.equals(type)) return DOUBLE;
        if (Short.class.equals(type) || short.class.equals(type)) return SHORT;
        if (Float.class.equals(type) || float.class.equals(type)) return FLOAT;
        if (Integer.class.equals(type) || int.class.equals(type)) return INTEGER;
        if (Byte.class.equals(type) || byte.class.equals(type)) return BYTE;
        if (Character.class.equals(type) || char.class.equals(type)) return CHARACTER;
        if (Boolean.class.equals(type) || boolean.class.equals(type)) return BOOLEAN;
        if (Date.class.equals(type)) return DATE;
        if (LocalDateTime.class.equals(type)) return LOCAL_DATE_TIME;
        if (ZonedDateTime.class.equals(type)) return ZONED_DATE_TIME;
        if (Duration.class.equals(type)) return DURATION;
        if (List.class.isAssignableFrom(type)) return LIST;
        if (ImmutableArray.class.isAssignableFrom(type)) return IMMUTABLE_ARRAY;
        if (Set.class.isAssignableFrom(type)) return SET;
        if (ImmutableSet.class.isAssignableFrom(type)) return IMMUTABLE_SET;
        if (Queue.class.isAssignableFrom(type)) return QUEUE;
        if (Deque.class.isAssignableFrom(type)) return DEQUEUE;
        if (Collection.class.isAssignableFrom(type)) return COLLECTION;
        if (ImmutableCollection.class.isAssignableFrom(type)) return IMMUTABLE_COLLECTION;
        if (Map.class.isAssignableFrom(type)) return MAP;
        if (ImmutableMap.class.isAssignableFrom(type)) return IMMUTABLE_MAP;
        if (Stream.class.isAssignableFrom(type)) return STREAM;
        if (Flux.class.isAssignableFrom(type)) return FLUX;
        if (Mono.class.isAssignableFrom(type)) return MONO;
        if (LazyProperty.class.equals(type)) return LAZY;
        if (Optional.class.equals(type)) return OPTIONAL;
        if (Supplier.class.equals(type)) return SUPPLIER;
        if (InputStream.class.isAssignableFrom(type)) return INPUT_STREAM;
        if (OutputStream.class.isAssignableFrom(type)) return INPUT_STREAM;
        if (ByteBuf.class.isAssignableFrom(type)) return NETTY_BUFFER;
        if (ByteBuffer.class.isAssignableFrom(type)) return NIO_BUFFER;
        return CUSTOM;
    }
}
