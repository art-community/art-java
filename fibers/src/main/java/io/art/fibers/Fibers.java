package io.art.fibers;

import static io.art.fibers.service.FiberService.*;
import java.time.*;

public class Fibers {
    private static boolean done = false;
    private static LocalDateTime start = LocalDateTime.now();

    public static void main(String[] args) {
        createFiber(Fibers::test1);
        while (!done) {
            if (LocalDateTime.now().isAfter(start.plusSeconds(300))) {
                done = true;
            }
            suspend();
        }
    }

    public static void test1() {
        while (!done) {
            suspend();
        }
    }

    public static void test2() {
        System.out.println("input 2");
    }
}
