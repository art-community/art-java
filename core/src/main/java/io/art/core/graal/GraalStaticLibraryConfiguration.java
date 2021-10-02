package io.art.core.graal;

import lombok.*;

@Getter
@Builder
public class GraalStaticLibraryConfiguration {
    private final String[] libraryNames;
    private final String[] symbolPrefixes;
}
