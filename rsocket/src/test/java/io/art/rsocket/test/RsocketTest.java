package io.art.rsocket.test;

import io.art.meta.test.meta.*;
import io.art.rsocket.test.communicator.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.message.pack.module.MessagePackActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.Rsocket.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.art.rsocket.test.communicator.TestRsocket.*;
import static io.art.rsocket.test.registry.RsocketTestExecutionsRegistry.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class RsocketTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                transport(),
                messagePack(),
                rsocket(rsocket -> rsocket
                        .communicator(communicator -> communicator.tcp(TestRsocketConnector.class))
                        .server(server -> server.tcp().configureService(TestRsocketService.class)))
        );
    }

    @Test
    public void testRsocket() {
        clear();

        TestRsocketConnector connector = rsocketConnector(TestRsocketConnector.class);
        TestRsocket communicator = connector.testRsocket();

        communicator.m1();
        assertEquals("test", communicator.m2(), "m2");
        assertEquals("test", asMono(communicator.m3()).block(), "m3");
        assertEquals("test", asFlux(communicator.m4()).blockFirst(), "m4");
        communicator.m5("test");
        assertEquals("test", communicator.m6("test"), "m6");
        assertEquals("test", asMono(communicator.m7("test")).block(), "m7");
        assertEquals("test", asFlux(communicator.m8("test")).blockFirst(), "m8");
        communicator.m9(Mono.just("test"));
        assertEquals("test", communicator.m10(Mono.just("test")), "m10");
        assertEquals("test", asMono(communicator.m11(Mono.just("test"))).block(), "m11");
        assertEquals("test", asFlux(communicator.m12(Mono.just("test"))).blockFirst(), "m12");
        communicator.m13(Flux.just("test"));
        assertEquals("test", communicator.m14(Flux.just("test")), "m14");
        assertEquals("test", asMono(communicator.m15(Flux.just("test"))).block(), "m15");
        assertEquals("test", asFlux(communicator.m16(Flux.just("test"))).blockFirst(), "m16");

        Map<String, Object> executions = executions();
        assertNotNull(executions.get("m1"), "m1");
        assertNotNull(executions.get("m2"), "m2");
        assertNotNull(executions.get("m3"), "m3");
        assertNotNull(executions.get("m4"), "m4");
        assertEquals("test", executions.get("m5"), "m5");
        assertEquals("test", executions.get("m6"), "m6");
        assertEquals("test", executions.get("m7"), "m7");
        assertEquals("test", executions.get("m8"), "m8");
        assertEquals("test", asMono(executions.get("m9")).block(), "m9");
        assertEquals("test", asMono(executions.get("m10")).block(), "m10");
        assertEquals("test", asMono(executions.get("m11")).block(), "m11");
        assertEquals("test", asMono(executions.get("m12")).block(), "m12");
        assertEquals("test", asFlux(executions.get("m13")).blockFirst(), "m13");
        assertEquals("test", asFlux(executions.get("m14")).blockFirst(), "m14");
        assertEquals("test", asFlux(executions.get("m15")).blockFirst(), "m15");
        assertEquals("test", asFlux(executions.get("m16")).blockFirst(), "m16");
    }
}
