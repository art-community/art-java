package io.art.tarantool.state;

import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import java.util.concurrent.atomic.*;


public class TarantoolRequestIdState {
    private final static AtomicInteger id = new AtomicInteger();
    private final static int authenticationId = nextId();

    public static int nextId() {
        return id.addAndGet(REQUEST_STEP);
    }

    public static void decrementId() {
        id.addAndGet(-REQUEST_STEP);
    }

    public static int authenticationId() {
        return authenticationId;
    }
}
