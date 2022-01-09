package io.art.core.graal;

import lombok.*;
import static io.art.core.extensions.JarExtensions.*;
import static io.art.core.graal.GraalConfiguration.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.nio.file.Files.*;
import java.nio.file.*;
import java.util.*;

@Builder
public class GraalNativeLibraryLocation {
    private final String extractionDirectory;

    @Singular("resource")
    private final Set<String> resources;

    public Path resolve() {
        Path libraryDirectory = Paths.get(GRAAL_WORKING_PATH).resolve(extractionDirectory).toAbsolutePath();
        ignoreException(() -> createDirectories(libraryDirectory));
        for (String regex : resources) {
            extractCurrentJarEntry(GraalNativeLibraryConfiguration.class, regex, libraryDirectory.toString());
        }
        return libraryDirectory;
    }
}
