package ru.adk.core.extension;

import ru.adk.core.exception.InternalRuntimeException;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.channels.Channels.newChannel;
import static java.util.Collections.emptyList;
import static ru.adk.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.adk.core.constants.BufferConstants.MAX_FILE_BUFFER_SIZE;
import static ru.adk.core.constants.StreamConstants.EOF;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.factory.CollectionsFactory.arrayOf;
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
