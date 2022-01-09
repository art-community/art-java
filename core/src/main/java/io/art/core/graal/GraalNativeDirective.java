package io.art.core.graal;

import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.NativeConstants.*;
import static io.art.core.constants.RegExps.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.determiner.SystemDeterminer.*;
import java.nio.file.*;
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
