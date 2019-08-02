/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.core.extension;

import ru.art.core.exception.InternalRuntimeException;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.channels.Channels.newChannel;
import static java.util.Collections.emptyList;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.core.constants.BufferConstants.MAX_FILE_BUFFER_SIZE;
import static ru.art.core.constants.StreamConstants.EOF;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.factory.CollectionsFactory.arrayOf;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.List;

public interface InputStreamExtensions {
    static List<Byte> toByteList(InputStream is) {
        try {
            List<Byte> byteList = arrayOf(is.available());
            for (int i = is.read(); i != EOF; i = is.read()) {
                byteList.add((byte) i);
            }
            return byteList;
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
    }

    static List<Byte> toByteListSafety(InputStream is) {
        try {
            List<Byte> byteList = arrayOf(is.available());
            for (int i = is.read(); i != EOF; i = is.read()) {
                byteList.add((byte) i);
            }
            return byteList;
        } catch (IOException e) {
            e.printStackTrace();
            return emptyList();
        }
    }

    static byte[] toByteArray(InputStream is) {
        ByteBuffer buffer = allocateDirect(MAX_FILE_BUFFER_SIZE);
        try {
            ReadableByteChannel channel = newChannel(is);
            while (channel.read(buffer) > 0) {
                buffer.flip();
            }
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
        byte[] result = new byte[buffer.limit()];
        buffer.get(result);
        return result;
    }

    static byte[] toByteArraySafety(InputStream is) {
        try {
            return toByteArray(is);
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY_BYTES;
        }
    }

    static String toString(InputStream is, Charset charset) {
        return new String(toByteArray(is), charset);
    }

    static String toString(InputStream is) {
        try {
            return toString(is, contextConfiguration().getCharset());
        } catch (Exception e) {
            e.printStackTrace();
            return EMPTY_STRING;
        }
    }
}
