package io.art.tarantool.state;

import java.util.concurrent.atomic.*;


public class TarantoolRequestIdState {
    private final static AtomicInteger id = new AtomicInteger();
    private final static int authenticationId = nextId();

    public static int nextId() {
        return id.incrementAndGet();
    }

    public static int authenticationId() {
        return authenticationId;
    }

    public static int decrementId() {
        return id.decrementAndGet();
    }
}
