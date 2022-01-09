package io.art.core.graal;

import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.GraalConstants.*;
import static io.art.core.constants.NativeConstants.*;
import static io.art.core.constants.RegExps.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.determiner.SystemDeterminer.*;
import java.nio.file.*;

@Builder(builderMethodName = "singleLibrary")
public class GraalSingleLibrary {
    private final String customExtractionDirectory;
    private final String libraryFileName;
    private final String headerFileName;

    public GraalNativeDirective.GraalNativeDirectiveBuilder directive() {
        GraalNativeLibraryLocation.GraalNativeLibraryLocationBuilder locationBuilder = GraalNativeLibraryLocation.builder()
                .extractionDirectory(ifEmpty(customExtractionDirectory, libraryFileName + GRAAL_LIBRARY_EXTRACTION_DIRECTORY_POSTFIX))
                .resource(nativeHeaderRegexp(headerFileName));

        if (isUnix() || isMac()) {
            locationBuilder.resource(nativeUnixStaticLibraryRegex(libraryFileName));
        }

        if (isWindows()) {
            locationBuilder.resource(nativeWindowsStaticLibraryRegex(libraryFileName));
        }

        Path location = locationBuilder.build().resolve();

        return GraalNativeDirective.builder()
                .libraryPath(location.toAbsolutePath().toString())
                .header(DOUBLE_QUOTES + location.toAbsolutePath().resolve(headerFileName + HEADER_FILE_EXTENSION) + DOUBLE_QUOTES)
                .library(libraryFileName);
    }
}
