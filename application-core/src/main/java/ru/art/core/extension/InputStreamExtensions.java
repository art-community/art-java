/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.extension;

import lombok.experimental.*;
import ru.art.core.exception.*;
import static java.lang.System.*;
import static java.nio.ByteBuffer.*;
import static java.nio.channels.Channels.*;
import static java.util.Collections.*;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.core.constants.BufferConstants.*;
import static ru.art.core.constants.StreamConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.factory.CollectionsFactory.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

@UtilityClass
public class InputStreamExtensions {
    public static List<Byte> toByteList(InputStream is) {
        try {
            List<Byte> byteList = arrayOf(is.available());
            for (int i = is.read(); i != EOF; i = is.read()) {
                byteList.add((byte) i);
            }
            return byteList;
        } catch (IOException ioException) {
            throw new InternalRuntimeException(ioException);
        }
    }

    public static List<Byte> toByteListSafety(InputStream is) {
        try {
            List<Byte> byteList = arrayOf(is.available());
            for (int i = is.read(); i != EOF; i = is.read()) {
                byteList.add((byte) i);
            }
            return byteList;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return emptyList();
        }
    }

    public static byte[] toByteArray(InputStream is) {
        return toByteArray(is, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] toByteArray(InputStream is, int bufferSize) {
        ByteBuffer buffer = allocateDirect(bufferSize);
        byte[] result = EMPTY_BYTES;
        try {
            ReadableByteChannel channel = newChannel(is);
            while (channel.read(buffer) != EOF) {
                buffer.flip();
                if (result.length == 0 && buffer.limit() <= bufferSize) {
                    result = new byte[buffer.limit()];
                    buffer.get(result);
                    return result;
                }
                byte[] bufferBytes = new byte[buffer.limit()];
                buffer.get(bufferBytes);
                byte[] newResult = new byte[result.length + bufferBytes.length];
                arraycopy(result, 0, newResult, 0, result.length);
                arraycopy(bufferBytes, 0, newResult, result.length, bufferBytes.length);
                result = newResult;
                buffer.clear();
            }
        } catch (IOException ioException) {
            throw new InternalRuntimeException(ioException);
        }
        return result;
    }

    public static byte[] toByteArraySafety(InputStream is) {
        try {
            return toByteArray(is);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return EMPTY_BYTES;
        }
    }

    public static String toString(InputStream is, Charset charset) {
        return new String(toByteArray(is), charset);
    }

    public static String toString(InputStream is) {
        try {
            return toString(is, contextConfiguration().getCharset());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return EMPTY_STRING;
        }
    }
}
