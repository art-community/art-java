package io.art.rsocket.test;

import io.art.meta.test.meta.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.Rsocket.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.art.rsocket.test.communicator.TestRsocket.*;
import java.util.concurrent.*;

public class RsocketTest {
    private final static CountDownLatch waiter = new CountDownLatch(4);

    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                json(),
                rsocket(rsocket -> rsocket
                        .communicator(communicator -> communicator.tcp(TestRsocketConnector.class))
                        .server(server -> server.tcp().configureService(TestRsocketService.class)))
        );
    }

    @Test
    public void test() {
        TestRsocketConnector tcp = rsocketConnector(TestRsocketConnector.class);
        tcp.testRsocket().m1("test");
        tcp.testRsocket().m1("test");
        tcp.testRsocket().m2("test");
        tcp.testRsocket().m2("test");
        ignoreException(waiter::await);
    }
}
