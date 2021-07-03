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

public interface Writer {
    default void write(TypedObject model, ByteBuffer buffer, Function<IOException, ? extends RuntimeException> catcher) {
        try (NioByteBufferOutputStream outputStream = new NioByteBufferOutputStream(buffer)) {
            write(model, outputStream);
        } catch (IOException ioException) {
            throw catcher.apply(ioException);
        }
    }

    default void write(TypedObject model, ByteBuf buffer, Function<IOException, ? extends RuntimeException> catcher) {
        try (ByteBufOutputStream outputStream = new ByteBufOutputStream(buffer)) {
            write(model, outputStream);
        } catch (IOException ioException) {
            throw catcher.apply(ioException);
        }
    }

    default byte[] writeToBytes(TypedObject model) {
        ByteBuffer buffer = allocate(DEFAULT_BUFFER_SIZE);
        try {
            return writeToBytes(model, buffer);
        } finally {
            buffer.clear();
        }
    }

    default byte[] writeToBytes(TypedObject model, ByteBuffer buffer) {
        write(model, buffer);
        return toByteArray(buffer);
    }

    default String writeToString(TypedObject model) {
        return writeToString(model, context().configuration().getCharset());
    }

    default String writeToString(TypedObject model, ByteBuffer buffer) {
        return writeToString(model, buffer, context().configuration().getCharset());
    }

    default String writeToString(TypedObject model, Charset charset) {
        return new String(writeToBytes(model), charset);
    }

    default String writeToString(TypedObject model, ByteBuffer buffer, Charset charset) {
        return new String(writeToBytes(model, buffer), charset);
    }

    default void write(TypedObject model, OutputStream outputStream) {
        write(model, outputStream, context().configuration().getCharset());
    }

    void write(TypedObject model, OutputStream outputStream, Charset charset);

    void write(TypedObject model, ByteBuffer buffer);

    void write(TypedObject model, ByteBuf buffer);
}
