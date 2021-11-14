package io.art.core.file;

import lombok.*;
import static io.art.core.extensions.FileExtensions.*;
import java.io.*;
import java.util.function.*;

@Getter
@AllArgsConstructor
public class FileProxy {
    private final String path;
    private final Supplier<InputStream> inputStream;

    public FileProxy(File file) {
        path = file.getAbsolutePath();
        inputStream = () -> fileInputStream(file);
    }
}
