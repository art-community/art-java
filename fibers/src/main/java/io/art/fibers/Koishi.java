package io.art.fibers;

import com.oracle.svm.core.c.*;
import io.art.core.graal.*;
import org.graalvm.nativeimage.c.*;
import org.graalvm.nativeimage.c.constant.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.nativeimage.c.struct.*;
import org.graalvm.nativeimage.c.type.*;
import org.graalvm.word.*;
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

    @CFunction(value = "koishi_init")
    public static native void koishi_init(koishi_coroutine_t co, int min_stack_size, WordBase entry_point);

    @CFunction(value = "koishi_resume")
    public static native VoidPointer koishi_resume(koishi_coroutine_t co, Fibers.FiberStartData data);

    @CFunction(value = "koishi_yield")
    public static native VoidPointer koishi_yield(VoidPointer arg);

    @CFunction(value = "koishi_create")
    public static native koishi_coroutine_t koishi_create();

    @CFunction(value = "koishi_destroy")
    public static native void koishi_destroy(koishi_coroutine_t pointer);

    @CEnum(value = "koishi_state", addEnumKeyword = true)
    public enum koishi_state {
        KOISHI_SUSPENDED,
        KOISHI_RUNNING,
        KOISHI_DEAD
    }

    @CStruct(addStructKeyword = true, value = "koishi_coroutine", isIncomplete = true)
    public interface koishi_coroutine extends PointerBase {

    }

    @CPointerTo(koishi_coroutine.class)
    @CTypedef(name = "koishi_coroutine_t")
    public interface koishi_coroutine_t extends PointerBase {
    }


    public static CEntryPointLiteral<CFunctionPointer> runFiber = CEntryPointLiteral.create(Fibers.class, "runFiber", Fibers.FiberStartData.class);
}
