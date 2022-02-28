package io.art.tarantool.test.model;

import lombok.*;
import reactor.core.publisher.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.*;

public class TestService {
    private static final CountDownLatch WAITER = new CountDownLatch(4);

    @Getter
    @AllArgsConstructor
    public static class TestRequest {
        private final int intValue;
        private final String stringValue;
    }

    public static boolean await() {
        try {
            return WAITER.await(30, TimeUnit.SECONDS);
        } catch (Throwable throwable) {
            fail(throwable);
        }
        return false;
    }

    public void testEmpty() {
        WAITER.countDown();
    }

    public void testRequest(TestRequest request) {
        assertEquals(request.intValue, 1);
        assertEquals(request.stringValue, "test");
        WAITER.countDown();
    }

    public void testChannel(Flux<TestRequest> channel) {
        channel.subscribe(value -> {
            assertEquals(value.intValue, 1);
            assertEquals(value.stringValue, "test");
            WAITER.countDown();
        });
    }
}
