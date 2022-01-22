package io.art.rsocket.test;

import io.art.meta.test.meta.*;
import io.art.rsocket.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.meta.MetaRsocketTest.MetaIoPackage.MetaArtPackage.MetaRsocketPackage.MetaTestPackage.MetaServicePackage.*;
import io.art.rsocket.test.registry.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.message.pack.module.MessagePackActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.art.rsocket.test.registry.RsocketTestExecutionsRegistry.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;

public class RsocketDefaultTest {
    @BeforeAll
    public static void setup() {
        RsocketTestExecutionsRegistry.clear();
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                transport(),
                messagePack(),
                rsocket(rsocket -> rsocket.server(server -> server.tcp().service(TestRsocketService.class)))
        );
    }

    @AfterAll
    public static void cleanup() {
        RsocketTestExecutionsRegistry.clear();
        shutdown();
    }

    @Test
    public void testDefaultRsocket() {
        Rsocket.rsocket()
                .tcp()
                .client(LOCALHOST, DEFAULT_PORT)
                .target(TestRsocketService.class, MetaTestRsocketServiceClass::m1Method)
                .fireAndForget();
        assertNotNull(executions(1).get("m1"));
    }
}
