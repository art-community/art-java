package io.art.rsocket.test;

import io.art.core.extensions.*;
import io.art.meta.test.meta.*;
import io.art.rsocket.test.communicator.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.extensions.OptionalExtensions.unwrap;
import static io.art.core.initializer.Initializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
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
                                .tcp(TestRsocketCommunicator.class, tcp -> tcp.single(client -> client.common(builder -> builder.port(1234))))
                                .forPackage(() -> unwrap(library().packageOf("io.art.rsocket.test.communicator")), configurator -> configurator.target(TestRsocketService.class)))
                        .server(server -> server
                                .tcp(tcp -> tcp.common(builder -> builder.port(1234)))
                                .forPackage(() -> unwrap(library().packageOf("io.art.rsocket.test.service")))))
        );
    }

    @Test
    public void test() {
        TestRsocketCommunicator communicator = rsocketCommunicator(TestRsocketCommunicator.class);
        communicator.m("test");
    }
}
