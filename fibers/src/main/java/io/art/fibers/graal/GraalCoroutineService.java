package io.art.fibers.graal;

import lombok.*;
import lombok.experimental.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.word.*;
import static com.oracle.svm.core.graal.snippets.StackOverflowCheckImpl.*;
import static io.art.fibers.constants.FiberConstants.Defaults.*;
import static io.art.fibers.graal.GraalCoroutine.*;

@UtilityClass
public class GraalCoroutineService {
    private final ThreadLocal<Fiber> current = new ThreadLocal<>();

    public static void initializeCoroutine(Runnable entryPoint) {
        coroutine_t coroutine = coroutine_create();
        stackBoundaryTL.set(WordFactory.unsigned(DEFAULT_FIBER_STACK_SIZE + singleton().yellowAndRedZoneSize()));
        coroutine_init(coroutine, DEFAULT_FIBER_STACK_SIZE, invokeFiber.getFunctionPointer(), CurrentIsolate.getCurrentThread());
        Fiber fiber = new Fiber(entryPoint, coroutine, Fiber.State.RESUMED);
        current.set(fiber);
        coroutine_resume(coroutine, ObjectHandles.getGlobal().create(fiber));
    }

    public static void suspendCoroutine() {
        Fiber fiber = current.get();
        switch (fiber.state) {
            case SUSPENDED:
                fiber.state = Fiber.State.RESUMED;
                coroutine_resume(fiber.coroutine, WordFactory.nullPointer());
                break;
            case RESUMED:
                fiber.state = Fiber.State.SUSPENDED;
                coroutine_yield(WordFactory.nullPointer());
                break;
        }
    }


    public static void destroyCoroutine() {
        coroutine_deinit(current.get().coroutine);
        coroutine_destroy(current.get().coroutine);
        singleton().updateStackOverflowBoundary();
    }

    @CEntryPoint
    public static void invoke(IsolateThread thread, ObjectHandle input) {
        Fiber fiber = ObjectHandles.getGlobal().get(input);
        fiber.run();
    }

    @AllArgsConstructor
    private static class Fiber {
        final Runnable main;
        final coroutine_t coroutine;
        State state;

        enum State {
            SUSPENDED,
            RESUMED
        }

        public void run() {
            main.run();
        }
    }

}
