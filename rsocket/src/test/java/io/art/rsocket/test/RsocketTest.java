package io.art.rsocket.test;

import io.art.meta.test.meta.*;
import io.art.rsocket.test.communicator.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.Rsocket.*;
import static io.art.rsocket.module.RsocketActivator.*;

public class RsocketTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                json(),
                rsocket(rsocket -> rsocket
                        .communicator(communicator -> communicator
                                .tcp(TestRsocketConnector1.class)
                                .http(TestRsocketConnector2.class, configurator -> configurator
                                        .weighted(builder -> builder
                                                .client(client -> client.port(9001))))
                        )
                        .server(server -> server
                                .tcp()
                                .http(http -> http.port(9001))
                                .configureClass(TestRsocketService.class)
                                .configureClass(TestRsocketService1.class)
                        )
                )
        );
    }

    @Test
    public void test() {
        TestRsocketConnector1 tcp = rsocketConnector(TestRsocketConnector1.class);
        tcp.testRsocket1().m1("test");
    }
}
