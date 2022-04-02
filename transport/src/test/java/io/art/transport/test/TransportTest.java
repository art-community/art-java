package io.art.transport.test;

import org.junit.jupiter.api.*;
import reactor.core.*;
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
import static reactor.netty.ConnectionObserver.State.*;
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
        int port = 9100;

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

    @Test
    public void testDisposing() throws InterruptedException {
        int port = 9101;
        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<DisposableServer> server = new AtomicReference<>();
        AtomicReference<DisposableChannel> client = new AtomicReference<>();

        TcpServer.create()
                .host(LOCALHOST)
                .port(port)
                .observe((connection, newState) -> {
                    if (newState == CONNECTED) latch.countDown();
                })
                .bind()
                .doOnNext(server::set)
                .subscribe();

        TcpClient.create()
                .host(LOCALHOST)
                .port(port)
                .doOnConnected(client::set)
                .doOnConnected(ignore -> latch.countDown())
                .connect()
                .subscribe();

        latch.await();
        client.getAndSet(null).dispose();
        server.getAndSet(null).disposeNow();
        waitCondition(() -> TCP.isPortAvailable(port));
    }
}
