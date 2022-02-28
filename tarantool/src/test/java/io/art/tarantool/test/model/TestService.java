package io.art.tarantool.test.model;

import io.art.tarantool.exception.*;
import lombok.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.*;

public class TestService {
    private static final CountDownLatch WAITER = new CountDownLatch(1);

    @Getter
    @AllArgsConstructor
    public static class TestRequest {
        private final int intValue;
        private final String stringValue;
    }

    public static void await() {
        wrapExceptionCall(() -> WAITER.await(10, TimeUnit.SECONDS), TarantoolException::new);
    }

    public void test(TestRequest request) {
        assertEquals(request.intValue, 1);
        assertEquals(request.stringValue, "test");
        WAITER.countDown();
    }
}
