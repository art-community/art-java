package io.art.core.graal;

import lombok.*;
import java.util.*;

@Getter
@Builder
public class GraalNativeDirective {
    @Singular("libraryPath")
    private final List<String> libraryPaths;

    @Singular("header")
    private final List<String> headers;

    @Singular("library")
    private final List<String> libraries;
}
