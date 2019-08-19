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

import lombok.experimental.UtilityClass;
import ru.art.core.exception.InternalRuntimeException;
import static java.lang.System.arraycopy;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteBuffer.wrap;
import static java.nio.channels.FileChannel.open;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardOpenOption.*;
import static java.util.Objects.isNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.art.core.constants.BufferConstants.DEFAULT_BUFFER_SIZE;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.context.Context.contextConfiguration;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class FileExtensions {
    static String readFile(String path) {
        return readFile(get(path), DEFAULT_BUFFER_SIZE);
    }

    public static String readFileQuietly(String path) {
        return readFileQuietly(get(path), DEFAULT_BUFFER_SIZE);
    }

    public static String readFile(Path path) {
        return readFile(path, DEFAULT_BUFFER_SIZE);
    }

    public static String readFileQuietly(Path path) {
        return readFileQuietly(path, DEFAULT_BUFFER_SIZE);
    }

    public static String readFile(String path, int bufferSize) {
        return readFile(get(path), bufferSize);
    }

    public static String readFile(Path path, int bufferSize) {
        ByteBuffer buffer = allocateDirect(bufferSize);
        StringBuilder result = new StringBuilder(EMPTY_STRING);
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(buffer);
                buffer.flip();
                result.append(contextConfiguration().getCharset().newDecoder().decode(buffer).toString());
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
        return result.toString();
    }

    public static String readFileQuietly(Path path, int bufferSize) {
        ByteBuffer buffer = allocateDirect(bufferSize);
        StringBuilder result = new StringBuilder(EMPTY_STRING);
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(buffer);
                buffer.flip();
                result.append(contextConfiguration().getCharset().newDecoder().decode(buffer).toString());
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public static byte[] readFileBytes(String path) {
        return readFileBytes(get(path), DEFAULT_BUFFER_SIZE);
    }

    public static byte[] readFileBytesQuietly(String path) {
        return readFileBytesQuietly(get(path), DEFAULT_BUFFER_SIZE);
    }

    public static byte[] readFileBytes(Path path) {
        return readFileBytes(path, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] readFileBytesQuietly(Path path) {
        return readFileBytesQuietly(path, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] readFileBytes(String path, int bufferSize) {
        return readFileBytes(get(path), bufferSize);
    }

    public static byte[] readFileBytes(Path path, int bufferSize) {
        ByteBuffer buffer = allocateDirect(bufferSize);
        byte[] result = EMPTY_BYTES;
        try {
            FileChannel fileChannel = open(path);
            do {
                fileChannel.read(buffer);
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
            } while (fileChannel.position() < fileChannel.size());
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
        return result;
    }

    public static byte[] readFileBytesQuietly(Path path, int bufferSize) {
        try {
            return readFileBytes(path, bufferSize);
        } catch (Throwable e) {
            return EMPTY_BYTES;
        }
    }


    public static void writeFile(String path, String content) {
        writeFile(get(path), content);
    }

    public static void writeFileQuietly(String path, String content) {
        writeFileQuietly(get(path), content);
    }

    public static void writeFile(Path path, String content) {
        writeFileQuietly(path, content.getBytes());
    }

    public static void writeFileQuietly(Path path, String content) {
        writeFileQuietly(path, content.getBytes());
    }


    public static void writeFile(String path, byte[] content) {
        writeFile(get(path), content);
    }

    public static void writeFileQuietly(String path, byte[] content) {
        writeFileQuietly(get(path), content);
    }

    public static void writeFile(Path path, byte[] content) {
        ByteBuffer byteBuffer = wrap(content);
        try {
            createDirectories(path.getParent());
            FileChannel fileChannel = open(path, CREATE, TRUNCATE_EXISTING, WRITE);
            fileChannel.write(byteBuffer);
            fileChannel.close();
        } catch (IOException e) {
            throw new InternalRuntimeException(e);
        }
    }

    public static void writeFileQuietly(Path path, byte[] content) {
        ByteBuffer byteBuffer = wrap(content);
        try {
            createDirectories(path.getParent());
            FileChannel fileChannel = open(path, CREATE, TRUNCATE_EXISTING, WRITE);
            fileChannel.write(byteBuffer);
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean deleteFileRecursive(String path) {
        return deleteFileRecursive(Paths.get(path));
    }

    public static boolean deleteFileRecursive(Path path) {
        return deleteFileRecursive(path.toFile());
    }

    @SuppressWarnings("all")
    public static boolean deleteFileRecursive(File file) {
        if (isNull(file) || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] children = file.listFiles();
        if (isEmpty(children)) {
            return file.delete();
        }
        for (File child : children) {
            deleteFileRecursive(child);
        }
        return file.delete();
    }
}
