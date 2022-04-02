package io.art.transport.test;

import io.art.core.network.selector.*;
import org.junit.jupiter.api.*;
import reactor.netty.tcp.*;
import static io.art.core.constants.NetworkConstants.LOCALHOST;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.network.selector.PortSelector.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class TransportTest {
    @BeforeAll
    public static void setup() {
        initialize(
                transport()
        );
    }

    @Test
    public void testRetry() throws InterruptedException {
        AtomicInteger retry = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        int port = findAvailableTcpPort();

        TcpClient.create()
                .host(LOCALHOST)
                .port(port)
                .doOnConnect(config -> {
                    if (retry.getAndIncrement() < 2) {
                        return;
                    }
                    TcpServer.create()
                            .port(port)
                            .doOnConnection(connection -> latch.countDown())
                            .bind()
                            .subscribe();
                })
                .connect()
                .retry(3)
                .subscribe();

        latch.await();
        assertEquals(3, retry.get());
    }
}
