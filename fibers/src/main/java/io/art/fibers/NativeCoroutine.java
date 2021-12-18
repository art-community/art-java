package io.art.fibers;

import com.oracle.svm.core.c.*;
import io.art.core.graal.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.*;
import org.graalvm.nativeimage.c.constant.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.nativeimage.c.struct.*;
import org.graalvm.nativeimage.c.type.*;
import org.graalvm.word.*;
import static io.art.core.constants.RegExps.*;
import java.nio.file.*;
import java.util.*;

@CContext(NativeCoroutine.Directives.class)
public class NativeCoroutine {
    public static final class Directives implements CContext.Directives {
        Path libraryDirectory = GraalNativeLibraryConfiguration.GraalNativeLibraryLocation.builder()
                .extractionDirectory("coroutine-library")
                .resource(nativeHeaderRegexp("coroutine"))
                .resource(nativeUnixStaticLibraryRegex("coroutine"))
                .build()
                .resolve();

        @Override
        public List<String> getLibraryPaths() {
            return Collections.singletonList(libraryDirectory.toString());
        }

        @Override
        public List<String> getHeaderFiles() {
            return Collections.singletonList("\"" + libraryDirectory.resolve("coroutine.h").toAbsolutePath() + "\"");
        }

        @Override
        public List<String> getLibraries() {
            return Collections.singletonList("coroutine");
        }
    }

    @CFunction("coroutine_util_page_size")
    public static native int coroutine_util_page_size();

    @CFunction(value = "coroutine_init")
    public static native void coroutine_init(coroutine_coroutine_t co, int min_stack_size, coroutine_entrypoint_t entry_point, IsolateThread thread);

    @CFunction(value = "coroutine_resume")
    public static native VoidPointer coroutine_resume(coroutine_coroutine_t co, ObjectHandle data);

    @CFunction(value = "coroutine_yield")
    public static native VoidPointer coroutine_yield(VoidPointer arg);

    @CFunction(value = "coroutine_create")
    public static native coroutine_coroutine_t coroutine_create();

    @CFunction(value = "coroutine_destroy")
    public static native void coroutine_destroy(coroutine_coroutine_t pointer);

    @CEnum(value = "coroutine_state", addEnumKeyword = true)
    public enum coroutine_state {
        SUSPENDED,
        RUNNING,
        DEAD
    }

    @CStruct(addStructKeyword = true, value = "coroutine_coroutine", isIncomplete = true)
    public interface coroutine_coroutine extends PointerBase {

    }

    @CPointerTo(coroutine_coroutine.class)
    @CTypedef(name = "coroutine_coroutine_t")
    public interface coroutine_coroutine_t extends PointerBase {
    }


    @CFunction("coroutine_dump")
    protected static native void coroutine_dump(int index);

    public interface coroutine_entrypoint_t extends CFunctionPointer {
        @InvokeCFunctionPointer
        void invoke(IsolateThread thread, ObjectHandle data);
    }

    public static CEntryPointLiteral<coroutine_entrypoint_t> runFiber = CEntryPointLiteral.create(Fibers.class, "runFiber", IsolateThread.class, ObjectHandle.class);
}
