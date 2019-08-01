package ru.adk.core.extension;

import static ru.adk.core.constants.StreamConstants.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface InputOutputStreamExtensions {
    static void transferBytes(InputStream bufferedInputStream, OutputStream outputStream, int bufferSize) throws IOException {
        byte[] bytes = new byte[bufferSize];
        int readChars;
        while ((readChars = bufferedInputStream.read(bytes)) != EOF) {
            outputStream.write(bytes, 0, readChars);
        }
    }
}
