package io.art.transport.test;

import org.junit.jupiter.api.*;
import reactor.netty.*;
import reactor.netty.tcp.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.network.selector.PortSelector.SocketType.*;
import static io.art.core.network.selector.PortSelector.*;
import static io.art.core.waiter.Waiter.*;
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

    @Test
    public void testRetry() throws InterruptedException {
        AtomicInteger retry = new AtomicInteger(0);
        CountDownLatch connectLatch = new CountDownLatch(1);
        AtomicReference<DisposableServer> disposableServer = new AtomicReference<>();
        AtomicReference<DisposableChannel> disposableClient = new AtomicReference<>();
        int port = findAvailableTcpPort();

        Runnable startServer = () -> TcpServer.create()
                .host(LOCALHOST)
                .port(port)
                .doOnConnection(connection -> connectLatch.countDown())
                .bind()
                .doOnNext(disposableServer::set)
                .subscribe();

        TcpClient.create()
                .host(LOCALHOST)
                .port(port)
                .doOnConnect(config -> {
                    if (retry.incrementAndGet() == 3) startServer.run();
                })
                .connect()
                .doOnNext(disposableClient::set)
                .retry(3)
                .subscribe();

        connectLatch.await();
        assertEquals(3, retry.get());
        disposableServer.getAndSet(null).disposeNow();
        disposableClient.getAndSet(null).disposeNow();
        waitCondition(() -> TCP.isPortAvailable(port));
    }
}
