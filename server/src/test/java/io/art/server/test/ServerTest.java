package io.art.server.test;

import io.art.server.test.meta.*;
import io.art.server.test.meta.MetaServerTest.MetaIoPackage.MetaArtPackage.MetaServerPackage.MetaTestPackage.MetaServicePackage.*;
import io.art.server.test.service.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.server.test.factory.TestServiceMethodFactory.*;
import static io.art.server.test.registry.ServiceTestExecutionsRegistry.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest {
    @BeforeAll
    public static void setup() {
        initialize(meta(MetaServerTest::new));
    }

    @Test
    public void testServiceMethodExecution() {
        MetaTestServiceClass serviceClass = cast(declaration(TestService.class));

        serviceMethod(serviceClass, serviceClass.m1Method()).serve(Flux.empty());
        assertNotNull(executions().get("m1"), "m1");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m2Method()).serve(Flux.empty()).blockFirst(), "m2");
        assertNotNull(executions().get("m2"), "m2");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m3Method()).serve(Flux.empty()).blockFirst(), "m3");
        assertNotNull(executions().get("m3"), "m3");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m4Method()).serve(Flux.empty()).blockFirst(), "m4");
        assertNotNull(executions().get("m4"), "m4");


        serviceMethod(serviceClass, serviceClass.m5Method()).serve(Flux.just("test"));
        assertEquals("test", executions().get("m5"), "m5");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m6Method()).serve(Flux.just("test")).blockFirst(), "m6");
        assertEquals("test", executions().get("m6"), "m6");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m7Method()).serve(Flux.just("test")).blockFirst(), "m7");
        assertEquals("test", executions().get("m7"), "m7");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m8Method()).serve(Flux.just("test")).blockFirst(), "m8");
        assertEquals("test", executions().get("m8"), "m8");


        serviceMethod(serviceClass, serviceClass.m9Method()).serve(Flux.just("test"));
        assertEquals("test", asMono(executions().get("m9")).block(), "m9");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m10Method()).serve(Flux.just("test")).blockFirst(), "m10");
        assertEquals("test", asMono(executions().get("m10")).block(), "m10");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m11Method()).serve(Flux.just("test")).blockFirst(), "m11");
        assertEquals("test", asMono(executions().get("m11")).block(), "m11");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m12Method()).serve(Flux.just("test")).blockFirst(), "m12");
        assertEquals("test", asMono(executions().get("m12")).block(), "m12");


        serviceMethod(serviceClass, serviceClass.m13Method()).serve(Flux.just("test"));
        assertEquals("test", asFlux(executions().get("m13")).blockFirst(), "m13");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m14Method()).serve(Flux.just("test")).blockFirst(), "m14");
        assertEquals("test", asFlux(executions().get("m14")).blockFirst(), "m14");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m15Method()).serve(Flux.just("test")).blockFirst(), "m15");
        assertEquals("test", asFlux(executions().get("m15")).blockFirst(), "m15");

        assertEquals("test", serviceMethod(serviceClass, serviceClass.m16Method()).serve(Flux.just("test")).blockFirst(), "m16");
        assertEquals("test", asFlux(executions().get("m16")).blockFirst(), "m16");
    }
}
