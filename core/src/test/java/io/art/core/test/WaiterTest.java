package io.art.core.test;

import org.junit.jupiter.api.*;
import static io.art.core.waiter.Waiter.*;
import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.function.*;

public class WaiterTest {
    @Test
    public void testWaitTime() {
        LocalDateTime current = now();
        LocalDateTime expected = now().minusSeconds(current.getSecond());
        waitTime(ofSeconds(1));
        assertTrue(now().isAfter(expected));
    }

    @Test
    public void testWaitCondition() {
        LocalDateTime current = now();

        Supplier<Boolean> condition = () -> now().isAfter(current.plusSeconds(1));

        assertTrue(waitCondition(condition));
    }

    @Test
    public void testWaitConditionOutOfTime() {
        LocalDateTime current = now();

        Supplier<Boolean> condition = () -> now().isAfter(current.plusSeconds(5));

        assertFalse(waitCondition(Duration.ofSeconds(1), condition));
    }
}
