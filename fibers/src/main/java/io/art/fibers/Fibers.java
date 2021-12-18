package io.art.fibers;

import com.oracle.svm.core.stack.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.word.*;
import static io.art.fibers.Koishi.*;

public class Fibers {
    public static void main(String[] args) {
        koishi_coroutine_t co = koishi_create();

        koishi_init(co, 2 * 1024 * 1024, runFiber.getFunctionPointer(), CurrentIsolate.getCurrentThread());
        StackOverflowCheck.singleton().disableStackOverflowChecksForFatalError();

        koishi_resume(co, ObjectHandles.getGlobal().create(new Fiber()));

        koishi_destroy(co);
    }

    @CEntryPoint
    public static void runFiber(IsolateThread thread, ObjectHandle data) {
        Fiber f = ObjectHandles.getGlobal().get(data);
        f.run();
        koishi_yield(WordFactory.nullPointer());
    }


    public static class Fiber {
        public void run() {
            System.out.println("run");
        }
    }

}
