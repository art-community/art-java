package io.art.communicator.test;

import io.art.communicator.test.meta.*;
import io.art.communicator.test.proxy.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.communicator.test.registry.CommunicatorTestExecutionsRegistry.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.meta.module.MetaActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class CommunicatorTest {
    @BeforeAll
    public static void setup() {
        initialize(meta(MetaCommunicatorTest::new));
    }

    @Test
    public void testCommunicatorActionExecution() {
        TestCommunicator communicator = preconfiguredCommunicatorProxy(TestCommunicator.class, TestCommunication::new).getProxy();
        Map<String, Object> executions = executions();

        communicator.m1();
        assertNotNull(executions.get("m1"), "m1");

        assertEquals("test", communicator.m2(), "m2");
        assertNotNull(executions.get("m2"), "m2");

        assertEquals("test", asMono(communicator.m3()).block(), "m3");
        assertNotNull(executions.get("m3"), "m3");

        assertEquals("test", asFlux(communicator.m4()).blockFirst(), "m4");
        assertNotNull(executions.get("m4"), "m4");

        communicator.m5("test");
        assertEquals("test", executions.get("m5"), "m5");

        assertEquals("test", communicator.m6("test"), "m6");
        assertEquals("test", executions.get("m6"), "m6");

        assertEquals("test", asMono(communicator.m7("test")).block(), "m7");
        assertEquals("test", executions.get("m7"), "m7");

        assertEquals("test", asFlux(communicator.m8("test")).blockFirst(), "m8");
        assertEquals("test", executions.get("m8"), "m8");

        communicator.m9(Mono.just("test"));
        assertEquals("test", executions.get("m9"), "m9");

        assertEquals("test", communicator.m10(Mono.just("test")), "m10");
        assertEquals("test", executions.get("m10"), "m10");

        assertEquals("test", asMono(communicator.m11(Mono.just("test"))).block(), "m11");
        assertEquals("test", executions.get("m11"), "m11");

        assertEquals("test", asFlux(communicator.m12(Mono.just("test"))).blockFirst(), "m12");
        assertEquals("test", executions.get("m12"), "m12");

        communicator.m13(Flux.just("test"));
        assertEquals("test", executions.get("m13"), "m13");

        assertEquals("test", communicator.m14(Flux.just("test")), "m14");
        assertEquals("test", executions.get("m14"), "m14");

        assertEquals("test", asMono(communicator.m15(Flux.just("test"))).block(), "m15");
        assertEquals("test", executions.get("m15"), "m15");

        assertEquals("test", asFlux(communicator.m16(Flux.just("test"))).blockFirst(), "m16");
        assertEquals("test", executions.get("m16"), "m16");
    }
}
