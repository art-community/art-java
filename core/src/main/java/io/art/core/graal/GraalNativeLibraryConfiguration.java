package io.art.core.graal;

import io.art.core.collection.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.JarExtensions.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.lang.System.*;
import static java.nio.file.Files.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@Getter
@Builder
public class GraalNativeLibraryConfiguration {
    private final String name;
    private final boolean builtin;
    private final Type type;
    private final ImmutableSet<String> builtinSymbolPrefixes;
    private final GraalNativeLibraryLocation location;

    public enum Type {
        DYNAMIC,
        STATIC
    }

    @Builder
    public static class GraalNativeLibraryLocation {
        private final String extractionDirectory;

        @Singular("library")
        private final Set<String> libraryFilesRegex;

        @Singular("include")
        private final Set<String> includeFilesRegex;

        public Path resolve() {
            String workingPath = orElse(getProperty(GRAAL_WORKING_PATH_PROPERTY), EMPTY_STRING);
            File libraryDirectory = new File(workingPath);
            ignoreException(() -> createDirectories(Paths.get(workingPath).resolve(extractionDirectory)));
            for (String regex : libraryFilesRegex) {
                extractCurrentJarEntry(GraalNativeLibraryConfiguration.class, regex, libraryDirectory.getAbsolutePath());
            }
            for (String regex : includeFilesRegex) {
                extractCurrentJarEntry(GraalNativeLibraryConfiguration.class, regex, libraryDirectory.getAbsolutePath());
            }
            return libraryDirectory.toPath().resolve(extractionDirectory).toAbsolutePath();
        }
    }
}
