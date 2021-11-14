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

package io.art.meta.descriptor;

import io.art.core.annotation.*;
import io.art.core.stream.*;
import io.art.meta.model.*;
import io.netty.buffer.*;
import static io.art.core.constants.BufferConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.NioBufferExtensions.*;
import static java.nio.ByteBuffer.*;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.function.*;

@Public
public interface Writer {
    default void write(TypedObject object, ByteBuffer buffer, Function<IOException, ? extends RuntimeException> catcher) {
        try (NioByteBufferOutputStream outputStream = new NioByteBufferOutputStream(buffer)) {
            write(object, outputStream);
        } catch (IOException ioException) {
            throw catcher.apply(ioException);
        }
    }

    default void write(TypedObject object, ByteBuf buffer, Function<IOException, ? extends RuntimeException> catcher) {
        try (ByteBufOutputStream outputStream = new ByteBufOutputStream(buffer)) {
            write(object, outputStream);
        } catch (IOException ioException) {
            throw catcher.apply(ioException);
        }
    }

    default byte[] writeToBytes(TypedObject object) {
        ByteBuffer buffer = allocate(DEFAULT_BUFFER_SIZE);
        try {
            return writeToBytes(object, buffer);
        } finally {
            buffer.clear();
        }
    }

    default byte[] writeToBytes(TypedObject object, ByteBuffer buffer) {
        write(object, buffer);
        return toByteArray(buffer);
    }

    default String writeToString(TypedObject object) {
        return writeToString(object, context().configuration().getCharset());
    }

    default String writeToString(TypedObject object, ByteBuffer buffer) {
        return writeToString(object, buffer, context().configuration().getCharset());
    }

    default String writeToString(TypedObject object, Charset charset) {
        return new String(writeToBytes(object), charset);
    }

    default String writeToString(TypedObject object, ByteBuffer buffer, Charset charset) {
        return new String(writeToBytes(object, buffer), charset);
    }

    default void write(TypedObject object, OutputStream outputStream) {
        write(object, outputStream, context().configuration().getCharset());
    }

    void write(TypedObject object, OutputStream outputStream, Charset charset);

    void write(TypedObject object, ByteBuffer buffer);

    void write(TypedObject object, ByteBuf buffer);
}
