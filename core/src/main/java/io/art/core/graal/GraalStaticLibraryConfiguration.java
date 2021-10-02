package io.art.core.graal;

import io.art.core.collection.*;
import lombok.*;
import java.nio.file.*;

@Getter
@Builder
public class GraalStaticLibraryConfiguration {
    private final Path libraryPath;
    private final String libraryName;
    private final ImmutableSet<String> symbolPrefixes;
}
