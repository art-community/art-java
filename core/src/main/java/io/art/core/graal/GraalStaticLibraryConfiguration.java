package io.art.core.graal;

import lombok.*;
import java.nio.file.*;

@Getter
@Builder
public class GraalStaticLibraryConfiguration {
    private final Path libraryPath;
    private final String libraryName;
    private final String[] symbolPrefixes;
}
