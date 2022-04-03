package io.art.core.test;

import org.junit.jupiter.api.*;
import static io.art.core.network.selector.PortSelector.SocketType.*;
import static io.art.core.waiter.Waiter.*;
import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;
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

    @Test
    public void testWaitPort() throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket(9999, 1);
        socket.setReuseAddress(true);


        Thread threadAccept = new Thread(() -> {
            waitTime(ofSeconds(1));
            try {
                socket.accept();
            } catch (Throwable ignored) {

            }
            assertTrue(socket.isBound());
        });
        threadAccept.start();

        assertTrue(waitCondition(() -> !TCP.isPortAvailable(9999)));

        Thread threadClose = new Thread(() -> {
            waitTime(ofSeconds(1));
            try {
                socket.close();
            } catch (Throwable ignored) {
            }
            assertTrue(socket.isClosed());
        });
        threadClose.start();

        assertTrue(waitCondition(() -> TCP.isPortAvailable(9999)));

        threadAccept.join();
        threadClose.join();
    }
}
