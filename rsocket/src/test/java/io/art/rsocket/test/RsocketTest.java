package io.art.rsocket.test;

import io.art.communicator.configurator.*;
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
import static io.art.rsocket.Rsocket.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;

public class RsocketTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                json(),
                rsocket(rsocket -> rsocket
                        .communicator(communicator -> communicator
                                .tcp(TestRsocketCommunicator.class, tcp -> tcp
                                        .weighted(group -> group.client(client -> client.common(builder -> builder.port(1234))))
                                        .configure(builder -> builder.logging(true).dataFormat(JSON)))
                                .forClass(TestRsocketCommunicator.class, CommunicatorActionConfigurator::loggable))
                        .server(server -> server
                                .tcp(tcp -> tcp.common(builder -> builder.port(1234)))
                                .forClass(TestRsocketService.class, ServiceMethodConfigurator::loggable)))
        );
    }

    @Test
    public void test() {
        TestRsocketCommunicator communicator = rsocketCommunicator(TestRsocketCommunicator.class);
        communicator.m("test");
        ThreadExtensions.block();
    }
}
