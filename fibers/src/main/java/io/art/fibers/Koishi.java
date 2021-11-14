package io.art.fibers;

import io.art.core.graal.*;
import org.graalvm.nativeimage.c.*;
import org.graalvm.nativeimage.c.function.*;
import static io.art.core.constants.RegExps.*;
import java.nio.file.*;
import java.util.*;

@CContext(Koishi.Directives.class)
public class Koishi {
    public static final class Directives implements CContext.Directives {
        Path libraryDirectory = GraalNativeLibraryConfiguration.GraalNativeLibraryLocation.builder()
                .extractionDirectory("koishi-library")
                .resource(nativeHeaderRegexp("koishi"))
                .resource(nativeUnixStaticLibraryRegex("koishi"))
                .build()
                .resolve();

        @Override
        public List<String> getLibraryPaths() {
            return Collections.singletonList(libraryDirectory.toString());
        }

        @Override
        public List<String> getHeaderFiles() {
            return Collections.singletonList("\"" + libraryDirectory.resolve("koishi.h").toAbsolutePath() + "\"");
        }

        @Override
        public List<String> getLibraries() {
            return Collections.singletonList("koishi");
        }
    }

    @CFunction("koishi_util_page_size")
    public static native int koishi_util_page_size();
}
