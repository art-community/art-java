package io.art.fibers;

import io.art.fibers.graal.*;
import org.graalvm.word.*;
import static io.art.fibers.graal.GraalCoroutine.*;
import static io.art.fibers.service.FiberService.*;
import java.util.concurrent.atomic.*;

public class Fibers {
    private final static int iterations = 5000000;
    private static final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {

        createFiber(Fibers::cofunc);

        long begin = System.nanoTime();

        while (counter.get() < iterations) {
            coroutine_resume(GraalCoroutineService.current.get().coroutine, WordFactory.nullPointer());
        }

        long end = System.nanoTime();

        int num_switches = iterations * 2;
        double time_ns = end - begin;
        double time_s = time_ns * 1e-9;
        double switches_per_second = num_switches / time_s;
        double ns_per_switch = time_ns / num_switches;

        System.out.println("Nano seconds per switch: " + ns_per_switch);
        System.out.println("Switches per second: " + switches_per_second);

        destroyFiber();
    }

    static void cofunc() {
        while (counter.incrementAndGet() < iterations) {
            coroutine_yield(WordFactory.nullPointer());
        }
    }
}
