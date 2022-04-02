package io.art.transport.test;

import org.junit.jupiter.api.*;
import reactor.core.*;
import reactor.netty.tcp.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
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

    @AfterAll
    public static void cleanup() {
        shutdown();
    }

    @RepeatedTest(10)
    public void testRetry() throws InterruptedException {
        AtomicInteger retry = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Disposable> server = new AtomicReference<>();
        int port = 9091;

        Runnable startServer = () -> server.getAndSet(TcpServer.create()
                .port(port)
                .doOnConnection(connection -> latch.countDown())
                .bind()
                .subscribe());

        Disposable client = TcpClient.create()
                .host(LOCALHOST)
                .port(port)
                .doOnConnect(config -> {
                    if (retry.incrementAndGet() >= 2) {
                        startServer.run();
                    }
                })
                .connect()
                .retry(3)
                .subscribe();

        latch.await();
        assertEquals(3, retry.get());
        client.dispose();
        server.getAndSet(null).dispose();
    }
}
