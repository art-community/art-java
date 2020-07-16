package io.art.json.descriptor;

import io.art.entity.immutable.Value;
import io.art.json.exception.JsonMappingException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import static java.util.Objects.isNull;
import static io.art.core.context.Context.contextConfiguration;
import static io.art.core.extensions.FileExtensions.writeFileQuietly;
import static io.art.json.module.JsonModule.jsonModule;

public class JsonEntityPrettyWriter {
    public static byte[] prettyWriteJsonToBytes(Value value) {
        return prettyWriteJson(value).getBytes(contextConfiguration().getCharset());
    }

    public static void prettyWriteJson(Value value, OutputStream outputStream) {
        if (isNull(outputStream)) {
            return;
        }
        try {
            outputStream.write(prettyWriteJson(value).getBytes(contextConfiguration().getCharset()));
        } catch (IOException ioException) {
            throw new JsonMappingException(ioException);
        }
    }

    public static void prettyWriteJson(Value value, Path path) {
        writeFileQuietly(path, prettyWriteJson(value));
    }

    public static String prettyWriteJson(Value value) {
        return JsonEntityWriter.writeJson(jsonModule().getObjectMapper().getFactory(), value, true);
    }
}
