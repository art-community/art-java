package io.art.json.descriptor;

import io.art.entity.immutable.*;
import io.art.json.exception.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.json.module.JsonModule.*;
import java.io.*;
import java.nio.file.*;

public class JsonEntityPrettyWriter {
    public static byte[] prettyWriteJsonToBytes(Value value) {
        return prettyWriteJson(value).getBytes(context().configuration().getCharset());
    }

    public static void prettyWriteJson(Value value, OutputStream outputStream) {
        try {
            outputStream.write(prettyWriteJson(value).getBytes(context().configuration().getCharset()));
        } catch (IOException ioException) {
            throw new JsonMappingException(ioException);
        }
    }

    public static void prettyWriteJson(Value value, Path path) {
        writeFileQuietly(path, prettyWriteJson(value));
    }

    public static String prettyWriteJson(Value value) {
        return JsonEntityWriter.writeJson(jsonModule().configuration().getObjectMapper().getFactory(), value, true);
    }
}
