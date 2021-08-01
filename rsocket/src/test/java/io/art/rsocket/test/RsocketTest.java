package io.art.rsocket.test;

import io.art.core.extensions.*;
import io.art.meta.test.meta.*;
import io.art.rsocket.test.communicator.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import io.art.server.configurator.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
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
                                .tcp("server", tcp -> tcp
                                        .weighted(group -> group
                                                .client(client -> client.port(1234).host("1.1.1.1"))
                                                .client(client -> client.port(1234).host("1.1.1.2"))))
                                .forClass(TestRsocketCommunicator.class, action -> action.to("server"))
                        )
                        .server(server -> server
                                .tcp(tcp -> tcp.port(1234).logging(false))
                                .http(http -> http.port(1234).logging(false))
                                .forClass(TestRsocketService.class, ServiceMethodConfigurator::loggable)))
        );
    }

    @Test
    public void test() {
        ThreadExtensions.block();
    }
}
