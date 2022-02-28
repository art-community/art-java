package io.art.tarantool.test.model;

import lombok.*;
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

    public static boolean await() {
        try {
            return WAITER.await(10, TimeUnit.SECONDS);
        } catch (Throwable throwable) {
            fail(throwable);
        }
        return false;
    }

    public void test(TestRequest request) {
        assertEquals(request.intValue, 1);
        assertEquals(request.stringValue, "test");
        WAITER.countDown();
    }
}
