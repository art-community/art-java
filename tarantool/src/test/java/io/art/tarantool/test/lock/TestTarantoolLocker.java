package io.art.tarantool.test.lock;

import java.util.concurrent.locks.*;

public class TestTarantoolLocker {
    private final static ReentrantLock afterLock = new ReentrantLock();

    public static void lock() {
        afterLock.lock();
    }

    public static void unlock() {
        afterLock.unlock();
    }
}
