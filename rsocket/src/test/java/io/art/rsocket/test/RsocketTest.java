package io.art.rsocket.test;

import io.art.communicator.configurator.*;
import io.art.core.extensions.*;
import io.art.meta.test.meta.*;
import io.art.rsocket.test.communicator.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.server.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.Rsocket.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.rsocket.SocketAcceptor.*;

public class RsocketTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                json(),
                rsocket(rsocket -> rsocket
                        .communicator(communicator -> communicator
                                .tcp(TestRsocketConnector1.class, configurator -> configurator
                                        .roundRobin(builder -> builder
                                                .client(client -> client.port(1234))
                                                .client(client -> client.port(5678))))
                                .configureCommunicator(TestRsocket.class, CommunicatorActionConfigurator::resilience)
                        )
                        .server(server -> server
                                .tcp()
                                .http(http -> http.port(9001))
                                .configureService(TestRsocketService.class)
                                .configureService(TestRsocketService1.class)
                        )
                )
        );

        RSocketServer
                .create(forFireAndForget(payload -> {
                    System.out.println("1234");
                    return Mono.empty();
                }))
                .bindNow(TcpServerTransport.create(1234));

        RSocketServer
                .create(forFireAndForget(payload -> {
                    System.out.println("5678");
                    return Mono.empty();
                }))
                .bindNow(TcpServerTransport.create(5678));
    }

    @Test
    public void test() {
        TestRsocketConnector1 tcp = rsocketConnector(TestRsocketConnector1.class);
        tcp.testRsocket1().m1("test");
        tcp.testRsocket1().m1("test");
        ThreadExtensions.block();
    }
}
