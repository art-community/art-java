package io.art.core.graal;

import io.art.core.collection.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.FileExtensions.*;
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

    public interface GraalNativeLibraryLocation {
        Path resolve();
    }

    @AllArgsConstructor
    public static class GraalNativeLibraryJarLocation implements GraalNativeLibraryLocation {
        private final String extractionDirectory;
        private final String libraryFileRegex;

        @Override
        public Path resolve() {
            String workingPath = orElse(getProperty(GRAAL_WORKING_PATH_PROPERTY), EMPTY_STRING);
            File libraryDirectory = new File(workingPath);
            ignoreException(() -> createDirectories(Paths.get(workingPath).resolve(extractionDirectory)));
            extractCurrentJarEntry(GraalNativeLibraryConfiguration.class, libraryFileRegex, libraryDirectory.getAbsolutePath());
            return libraryDirectory.toPath().resolve(extractionDirectory).toAbsolutePath();
        }
    }

    @AllArgsConstructor
    public static class GraalNativeLibraryFileLocation implements GraalNativeLibraryLocation {
        private final String destinationDirectory;
        private final Path sourceFile;
        private final Path destinationFile;

        @Override
        public Path resolve() {
            String workingPath = orElse(getProperty(GRAAL_WORKING_PATH_PROPERTY), EMPTY_STRING);
            File libraryDirectory = new File(workingPath);
            ignoreException(() -> createDirectories(Paths.get(workingPath).resolve(destinationDirectory)));
            Path destinationDirectoryPath = libraryDirectory.toPath().resolve(destinationDirectory).toAbsolutePath();
            recursiveCopy(sourceFile, destinationDirectoryPath.resolve(destinationFile));
            return destinationDirectoryPath;
        }
    }
}
