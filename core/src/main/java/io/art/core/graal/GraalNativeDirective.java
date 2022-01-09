package io.art.core.graal;

import lombok.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.NativeConstants.*;
import static io.art.core.constants.RegExps.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.determiner.SystemDeterminer.*;
import static io.art.core.graal.GraalNativeLibraryConfiguration.*;
import static io.art.core.graal.GraalNativeLibraryConfiguration.GraalNativeLibraryLocation.*;
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

    public static GraalNativeDirective.GraalNativeDirectiveBuilder singleLibrary(String libraryName) {
        return singleLibrary(Paths.get(libraryName + GRAAL_LIBRARY_EXTRACTION_DIRECTORY_POSTFIX), libraryName, libraryName);
    }

    public static GraalNativeDirective.GraalNativeDirectiveBuilder singleLibrary(Path extractionDirectory, String libraryName) {
        return singleLibrary(extractionDirectory, libraryName, libraryName);
    }

    public static GraalNativeDirective.GraalNativeDirectiveBuilder singleLibrary(String libraryName, String headerName) {
        return singleLibrary(Paths.get(libraryName + GRAAL_LIBRARY_EXTRACTION_DIRECTORY_POSTFIX), libraryName, headerName);
    }

    public static GraalNativeDirective.GraalNativeDirectiveBuilder singleLibrary(Path extractionDirectory, String libraryName, String headerName) {
        GraalNativeLibraryLocationBuilder locationBuilder = GraalNativeLibraryLocation.builder().extractionDirectory(extractionDirectory.toString()).resource(nativeHeaderRegexp(headerName));

        if (isUnix() || isMac()) {
            locationBuilder.resource(nativeUnixStaticLibraryRegex(libraryName));
        }

        if (isWindows()) {
            locationBuilder.resource(nativeWindowsStaticLibraryRegex(libraryName));
        }

        Path location = locationBuilder.build().resolve();

        return GraalNativeDirective.builder()
                .libraryPath(location.toAbsolutePath().toString())
                .header(DOUBLE_QUOTES + location.toAbsolutePath().resolve(headerName + HEADER_FILE_EXTENSION) + DOUBLE_QUOTES)
                .library(libraryName);
    }
}
