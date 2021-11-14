package io.art.fibers;

import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.nativeimage.c.type.*;
import static io.art.fibers.Koishi.*;

public class Fibers {
    public static void main(String[] args) {
        System.out.println(koishi_util_page_size());

        koishi_coroutine_t co = koishi_create();

        System.out.println(co.rawValue());

        koishi_init(co, 128 * 1024, runFiber.getFunctionPointer(), CurrentIsolate.getCurrentThread());

        System.out.println("Here we are!");

        koishi_resume(co, CTypeConversion.toCString("test").get());

        System.out.println("Whooaaaa!!!");

        koishi_destroy(co);
    }

    @CEntryPoint
    public static void runFiber(IsolateThread thread, CCharPointer data) {
    }

    public static void runner(ObjectHandle fiberHandle) {
        Fiber f = ObjectHandles.getGlobal().get(fiberHandle);
        ObjectHandles.getGlobal().destroy(fiberHandle);
        f.run();
    }


    public static class Fiber {
        public void run() {
            System.out.println("run");
        }
    }

}
