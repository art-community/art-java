package io.art.fibers;

import static io.art.fibers.service.FiberService.*;
import java.util.concurrent.atomic.*;

public class Fibers {
    private final static int iterations = 5000000;
    private static final AtomicInteger counter = new AtomicInteger();
    private static volatile boolean done = false;

    public static void main(String[] args) {
        long begin = System.nanoTime();

        createFiber(Fibers::fiber);

        while (!done) {

        }

        long end = System.nanoTime();

        int num_switches = iterations * 3;
        double time_ns = end - begin;
        double time_s = time_ns * 1e-9;
        double switches_per_second = num_switches / time_s;
        double ns_per_switch = time_ns / num_switches;

        System.out.println("Nano seconds per switch: " + ns_per_switch);
        System.out.println("Switches per second: " + switches_per_second);

        destroyFiber();
    }

    static void fiber() {
        cofunc1();
        cofunc2();
        while (!done) {
            suspend();
        }
    }

    static void cofunc1() {
        while (counter.get() < iterations) {
            suspend();
        }
        done = true;
    }

    static void cofunc2() {
        while (counter.incrementAndGet() < iterations) {
            suspend();
        }
        done = true;
    }
}
