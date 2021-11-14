package io.art.core.graal;

import io.art.core.collection.*;
import lombok.*;
import static io.art.core.extensions.JarExtensions.*;
import static io.art.core.graal.GraalConfiguration.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.nio.file.Files.*;
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
}
