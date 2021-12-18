package io.art.fibers;

import com.oracle.svm.core.stack.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.word.*;
import static com.oracle.svm.core.graal.snippets.StackOverflowCheckImpl.*;
import static io.art.fibers.Koishi.*;

public class Fibers {
    public static void main(String[] args) {
        koishi_coroutine_t co = koishi_create();
        int min_stack_size = 1024 * 1024;
        stackBoundaryTL.set(WordFactory.unsigned(min_stack_size + StackOverflowCheck.singleton().yellowAndRedZoneSize()));

        koishi_init(co, min_stack_size, runFiber.getFunctionPointer(), CurrentIsolate.getCurrentThread());
        System.out.println("[koishi]: inited");

        System.out.println("[koishi]: before 1 resume");
        koishi_resume(co, ObjectHandles.getGlobal().create(new Fiber()));
        System.out.println("[koishi]: after 1 resume");

        System.out.println("[koishi]: before 2 resume");
        koishi_resume(co, ObjectHandles.getGlobal().create(new Fiber()));
        System.out.println("[koishi]: after 2 resume");

        koishi_destroy(co);
    }

    @CEntryPoint
    public static void runFiber(IsolateThread thread, ObjectHandle data) {
        System.out.println("[koishi]: runFiber 1 resume");
        Fiber fiber = ObjectHandles.getGlobal().get(data);
        fiber.run();

        System.out.println("[koishi]: runFiber yield");
        koishi_yield(WordFactory.nullPointer());
        System.out.println("[koishi]: runFiber after yield");

        fiber.run();
    }


    public static class Fiber {
        public static int counter = 0;

        public void run() {
            System.out.println("Java Fiber run: " + counter++);
        }
    }

}
