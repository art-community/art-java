package io.art.fibers;

import static io.art.fibers.service.FiberService.*;

public class Fibers {
    public static void main(String[] args) {
        createFiber(Fibers::test1);
        System.out.println("suspend");
        suspend();
        System.out.println("after exit");
        destroyFiber();
    }

    public static void test1() {
        System.out.println("input");
        suspend();
        System.out.println("suspended");
        System.out.println("exit");
    }
}
