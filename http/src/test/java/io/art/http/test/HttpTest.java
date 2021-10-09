package io.art.http.test;

import io.art.http.test.communicator.*;
import io.art.http.test.meta.*;
import io.art.http.test.registry.*;
import io.art.http.test.service.*;
import io.art.meta.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.http.Http.*;
import static io.art.http.module.HttpActivator.*;
import static io.art.http.test.communicator.TestHttp.*;
import static io.art.http.test.registry.HttpTestExecutionsRegistry.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@Tag("http")
public class HttpTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaHttpTest(new MetaMetaTest())),
                transport(),
                json(),
                http(http -> http
                        .communicator(communicator -> communicator.connector(TestHttpConnector.class))
                        .server(server -> server.route(TestHttpService.class)))
        );
    }

    @AfterAll
    public static void cleanup() {
        HttpTestExecutionsRegistry.clear();
        shutdown();
    }

    @Test
    public void testHttp() {
        TestHttpConnector connector = httpConnector(TestHttpConnector.class);
        TestHttp communicator = connector.testHttp();

        communicator.post1();
        assertEquals("test", communicator.post2(), "post2");
        assertEquals("test", asMono(communicator.post3()).block(), "post3");
        assertEquals("test", asFlux(communicator.post4()).blockFirst(), "post4");
        communicator.post5("test");
        assertEquals("test", communicator.post6("test"), "post6");
        assertEquals("test", asMono(communicator.post7("test")).block(), "post7");
        assertEquals("test", asFlux(communicator.post8("test")).blockFirst(), "post8");
        communicator.post9(Mono.just("test"));
        assertEquals("test", communicator.post10(Mono.just("test")), "post10");
        assertEquals("test", asMono(communicator.post11(Mono.just("test"))).block(), "post11");
        assertEquals("test", asFlux(communicator.post12(Mono.just("test"))).blockFirst(), "post12");
        communicator.post13(Flux.just("test"));
        assertEquals("test", communicator.post14(Flux.just("test")), "post14");
        assertEquals("test", asMono(communicator.post15(Flux.just("test"))).block(), "post15");
        assertEquals("test", asFlux(communicator.post16(Flux.just("test"))).blockFirst(), "post16");

        Map<String, Object> executions = executions(Meta.declaration(TestHttp.class).methods().size());
        assertNotNull(executions.get("post1"), "post1");
        assertNotNull(executions.get("post2"), "post2");
        assertNotNull(executions.get("post3"), "post3");
        assertNotNull(executions.get("post4"), "post4");
        assertEquals("test", executions.get("post5"), "post5");
        assertEquals("test", executions.get("post6"), "post6");
        assertEquals("test", executions.get("post7"), "post7");
        assertEquals("test", executions.get("post8"), "post8");
        assertEquals("test", asMono(executions.get("post9")).block(), "post9");
        assertEquals("test", asMono(executions.get("post10")).block(), "post10");
        assertEquals("test", asMono(executions.get("post11")).block(), "post11");
        assertEquals("test", asMono(executions.get("post12")).block(), "post12");
        assertEquals("test", asFlux(executions.get("post13")).blockFirst(), "post13");
        assertEquals("test", asFlux(executions.get("post14")).blockFirst(), "post14");
        assertEquals("test", asFlux(executions.get("post15")).blockFirst(), "post15");
        assertEquals("test", asFlux(executions.get("post16")).blockFirst(), "post16");
    }
}
