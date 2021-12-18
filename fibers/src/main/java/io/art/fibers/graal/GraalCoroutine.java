package io.art.fibers.graal;

import com.oracle.svm.core.c.*;
import io.art.core.graal.*;
import lombok.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.*;
import org.graalvm.nativeimage.c.constant.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.nativeimage.c.struct.*;
import org.graalvm.nativeimage.c.type.*;
import org.graalvm.word.*;
import static io.art.core.graal.GraalNativeDirective.*;
import static io.art.fibers.constants.FiberConstants.GraalConstants.*;
import java.util.*;

@CContext(GraalCoroutine.Directives.class)
public class GraalCoroutine {
    @Getter
    public static final class Directives implements CContext.Directives {
        private final GraalNativeDirective directive = singleLibrary(COROUTINE_LIBRARY_NAME).build();
        private final List<String> headerFiles = directive.getHeaders();
        private final List<String> libraries = directive.getLibraries();
        private final List<String> libraryPaths = directive.getLibraryPaths();
    }

    @CFunction(value = "coroutine_init")
    public static native void coroutine_init(coroutine_t coroutine, int minimalStackSize, coroutine_entrypoint_t entryPoint, IsolateThread thread);

    @CFunction(value = "coroutine_resume")
    public static native VoidPointer coroutine_resume(coroutine_t coroutine, ObjectHandle input);

    @CFunction(value = "coroutine_yield")
    public static native VoidPointer coroutine_yield(VoidPointer output);

    @CFunction(value = "coroutine_create")
    public static native coroutine_t coroutine_create();

    @CFunction(value = "coroutine_destroy")
    public static native void coroutine_destroy(coroutine_t coroutine);

    @CFunction(value = "coroutine_deinit")
    public static native void coroutine_deinit(coroutine_t coroutine);

    @CFunction(value = "coroutine_recycle")
    public static native void coroutine_recycle(coroutine_t coroutine, coroutine_entrypoint_t entryPoint);

    @CFunction(value = "coroutine_die")
    public static native void coroutine_die(VoidPointer output);

    @CFunction(value = "coroutine_kill")
    public static native void coroutine_kill(coroutine_t coroutine, VoidPointer output);

    @CFunction(value = "coroutine_state")
    public static native coroutine_state coroutine_state(coroutine_t coroutine);

    @CFunction(value = "coroutine_active")
    public static native coroutine_t coroutine_active();

    @CEnum(value = "coroutine_state", addEnumKeyword = true)
    public enum coroutine_state {
        SUSPENDED,
        RUNNING,
        DEAD
    }

    @CStruct(addStructKeyword = true, value = "coroutine", isIncomplete = true)
    public interface coroutine extends PointerBase {

    }

    @CPointerTo(coroutine.class)
    @CTypedef(name = "coroutine_t")
    public interface coroutine_t extends PointerBase {
    }

    public interface coroutine_entrypoint_t extends CFunctionPointer {
        @InvokeCFunctionPointer
        void invoke(IsolateThread thread, ObjectHandle data);
    }


    public static final CEntryPointLiteral<coroutine_entrypoint_t> invokeFiber = CEntryPointLiteral.create(
            GraalCoroutineService.class,
            FIBER_INVOKE_METHOD_NAME,
            IsolateThread.class,
            ObjectHandle.class
    );
}
