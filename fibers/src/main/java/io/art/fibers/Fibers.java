package io.art.fibers;

import com.oracle.svm.core.c.function.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.nativeimage.c.struct.*;
import org.graalvm.word.*;
import static com.oracle.svm.core.c.function.CEntryPointSetup.*;
import static io.art.fibers.Koishi.*;
import javax.swing.text.*;

public class Fibers {
    public static void main(String[] args) {
        System.out.println(koishi_util_page_size());

        koishi_coroutine_t co = koishi_create();

        System.out.println(co.rawValue());

        koishi_init(co, 128 * 1024, runFiber.getFunctionPointer());

        System.out.println("Here we are!");

        FiberStartData data = UnmanagedMemory.malloc(SizeOf.get(FiberStartData.class));
        data.setFiberHandle(ObjectHandles.getGlobal().create(new Fiber()));
        data.setIsolate(CurrentIsolate.getIsolate());

        koishi_resume(co, data);

        System.out.println("Whooaaaa!!!");

        koishi_destroy(co);
    }

    private static class FiberStartRoutinePrologue {
        static void enter(FiberStartData data) {
            CEntryPointActions.enterIsolate(data.getIsolate());
        }
    }

    @CEntryPoint
    @CEntryPointOptions(prologue = FiberStartRoutinePrologue.class, publishAs = CEntryPointOptions.Publish.NotPublished, include = CEntryPointOptions.NotIncludedAutomatically.class)
    public static void runFiber(FiberStartData data) {
        Fiber f = ObjectHandles.getGlobal().get(data.getFiberHandle());
        UnmanagedMemory.free(data);
        f.run();
    }


    @RawStructure
    protected interface FiberStartData extends PointerBase {
        @RawField
        ObjectHandle getFiberHandle();

        @RawField
        void setFiberHandle(ObjectHandle handle);

        @RawField
        Isolate getIsolate();

        @RawField
        void setIsolate(Isolate vm);
    }


    public static class Fiber {
        public void run() {
            System.out.println("run");
        }
    }

}
