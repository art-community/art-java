package io.art.http.test;

import io.art.http.test.communicator.*;
import io.art.http.test.communicator.TestWs.*;
import io.art.http.test.meta.*;
import io.art.http.test.registry.*;
import io.art.http.test.service.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.http.Http.*;
import static io.art.http.module.HttpActivator.*;
import static io.art.http.test.registry.HttpTestExecutionsRegistry.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@Tag("http")
public class WsTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaHttpTest(new MetaMetaTest())),
                logging(logging -> logging.configureDefault(defaultLogger -> defaultLogger.enabled(true))),
                transport(),
                json(),
                http(http -> http
                        .communicator(communicator -> communicator.connector(TestWsConnector.class, connector -> connector.verbose(true)))
                        .server(server -> server.route(TestWsService.class).configure(s -> s.verbose(true)).service(TestWsService.class, ws -> ws.logging())))
        );
    }

    @AfterAll
    public static void cleanup() {
        HttpTestExecutionsRegistry.clear();
        shutdown();
    }

    @Test
    public void testWs() {
        TestWsConnector connector = httpConnector(TestWsConnector.class);
        TestWs communicator = connector.testWs();

        communicator.ws1();
        assertEquals("test", communicator.ws2(), "ws2");
        assertEquals("test", asMono(communicator.ws3()).block(), "ws3");
        assertEquals("test", asFlux(communicator.ws4()).blockFirst(), "ws4");
        communicator.ws5("test");
        assertEquals("test", communicator.ws6("test"), "ws6");
        assertEquals("test", asMono(communicator.ws7("test")).block(), "ws7");
        assertEquals("test", asFlux(communicator.ws8("test")).blockFirst(), "ws8");
        communicator.ws9(Mono.just("test"));
        assertEquals("test", communicator.ws10(Mono.just("test")), "ws10");
        assertEquals("test", asMono(communicator.ws11(Mono.just("test"))).block(), "ws11");
        assertEquals("test", asFlux(communicator.ws12(Mono.just("test"))).blockFirst(), "ws12");
        communicator.ws13(Flux.just("test"));
        assertEquals("test", communicator.ws14(Flux.just("test")), "ws14");
        assertEquals("test", asMono(communicator.ws15(Flux.just("test"))).block(), "ws15");
        assertEquals("test", asFlux(communicator.ws16(Flux.just("test"))).blockFirst(), "ws16");

        Map<String, Object> executions = executions();
        assertNotNull(executions.get("ws1"), "ws1");
        assertNotNull(executions.get("ws2"), "ws2");
        assertNotNull(executions.get("ws3"), "ws3");
        assertNotNull(executions.get("ws4"), "ws4");
        assertEquals("test", executions.get("ws5"), "ws5");
        assertEquals("test", executions.get("ws6"), "ws6");
        assertEquals("test", executions.get("ws7"), "ws7");
        assertEquals("test", executions.get("ws8"), "ws8");
        assertEquals("test", asMono(executions.get("ws9")).block(), "ws9");
        assertEquals("test", asMono(executions.get("ws10")).block(), "ws10");
        assertEquals("test", asMono(executions.get("ws11")).block(), "ws11");
        assertEquals("test", asMono(executions.get("ws12")).block(), "ws12");
        assertEquals("test", asFlux(executions.get("ws13")).blockFirst(), "ws13");
        assertEquals("test", asFlux(executions.get("ws14")).blockFirst(), "ws14");
        assertEquals("test", asFlux(executions.get("ws15")).blockFirst(), "ws15");
        assertEquals("test", asFlux(executions.get("ws16")).blockFirst(), "ws16");
    }
}
