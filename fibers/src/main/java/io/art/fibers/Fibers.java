package io.art.fibers;

import static io.art.fibers.service.FiberService.*;

public class Fibers {
    public static void main(String[] args) {
        createFiber(Fibers::test1);
        System.out.println("suspended");
        suspend();
        System.out.println("after exit");
        destroyFiber();
    }

    public static void test1() {
        System.out.println("input");
        suspend();
        System.out.println("resumed");
        test2();
        System.out.println("exit");
    }

    public static void test2() {
        System.out.println("input 2");
    }
}
