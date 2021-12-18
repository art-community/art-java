package io.art.fibers;

import com.oracle.svm.core.stack.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.word.*;
import static com.oracle.svm.core.graal.snippets.StackOverflowCheckImpl.*;
import static io.art.fibers.NativeCoroutine.*;

public class Fibers {
    public static void main(String[] args) {
        coroutine_coroutine_t co = coroutine_create();
        int min_stack_size = 1024 * 1024;
        stackBoundaryTL.set(WordFactory.unsigned(min_stack_size + StackOverflowCheck.singleton().yellowAndRedZoneSize()));

        coroutine_init(co, min_stack_size, runFiber.getFunctionPointer(), CurrentIsolate.getCurrentThread());
        System.out.println("[coroutine]: inited");

        System.out.println("[coroutine]: before 1 resume");
        coroutine_resume(co, ObjectHandles.getGlobal().create(new Fiber()));
        System.out.println("[coroutine]: after 1 resume");

        System.out.println("[coroutine]: before 2 resume");
        coroutine_resume(co, ObjectHandles.getGlobal().create(new Fiber()));
        System.out.println("[coroutine]: after 2 resume");

        coroutine_destroy(co);
    }

    @CEntryPoint
    public static void runFiber(IsolateThread thread, ObjectHandle data) {
        System.out.println("[coroutine]: runFiber 1 resume");
        Fiber fiber = ObjectHandles.getGlobal().get(data);
        fiber.run();

        System.out.println("[coroutine]: runFiber yield");
        coroutine_yield(WordFactory.nullPointer());
        System.out.println("[coroutine]: runFiber after yield");

        fiber.run();
    }


    public static class Fiber {
        public static int counter = 0;

        public void run() {
            System.out.println("Java Fiber run: " + counter++);
        }
    }

}
