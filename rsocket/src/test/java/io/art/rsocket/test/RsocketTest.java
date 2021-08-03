package io.art.rsocket.test;

import io.art.communicator.configurator.*;
import io.art.meta.test.meta.*;
import io.art.rsocket.test.communicator.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.meta.MetaRsocketTest.MetaIoPackage.MetaArtPackage.MetaRsocketPackage.MetaTestPackage.MetaCommunicatorPackage.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
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
                                .tcp(TestRsocketConnector1.class, connector -> connector
                                        .configure(builder -> builder.logging(true).service(asId(TestRsocket.class))))
                                .http(TestRsocketConnector2.class, connector -> connector
                                        .configure(builder -> builder.service(asId(TestRsocket.class)).logging(true))
                                        .single(client -> client.common(common -> common.port(9001))))
                                .configure(TestRsocket.class, MetaTestRsocketClass::m1Method, CommunicatorActionConfigurator::loggable))
                        .server(server -> server
                                .tcp()
                                .http(http -> http.common(common -> common.port(9001)))
                                .forClass(TestRsocketService.class))
                )
        );
    }

    @Test
    public void test() {
        TestRsocketConnector1 tcp = rsocketConnector(TestRsocketConnector1.class);
        tcp.testRsocket().m1("test");
        tcp.testRsocket().m2("test");

        TestRsocketConnector2 http = rsocketConnector(TestRsocketConnector2.class);
        http.testRsocket().m1("test");
        http.testRsocket().m2("test");
    }
}
