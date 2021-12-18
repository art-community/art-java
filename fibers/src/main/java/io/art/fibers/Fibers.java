package io.art.fibers;

import static io.art.fibers.service.FiberService.*;
import java.util.concurrent.atomic.*;

public class Fibers {
    private final static int iterations = 5000000;
    private static final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) {
        long begin = System.nanoTime();

        createFiber(Fibers::cofunc1);

        long end = System.nanoTime();

        int num_switches = iterations * 2;
        double time_ns = (end - begin) + (end - begin) * 1000000000L;
        double time_s = time_ns * 1e-9;
        double switches_per_second = num_switches / time_s;
        double ns_per_switch = time_ns / num_switches;

        System.out.println("Nano seconds per switch: " + ns_per_switch);
        System.out.println("Switches per second: " + switches_per_second);

        destroyFiber();
    }

    static void cofunc1() {
        cofunc2();
        while (counter.get() < iterations) {
            suspend();
        }
    }

    static void cofunc2() {
        while (counter.incrementAndGet() < iterations) {
            suspend();
        }
    }
}
