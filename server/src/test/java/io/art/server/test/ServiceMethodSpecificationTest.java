package io.art.server.test;

import io.art.server.test.meta.*;
import io.art.server.test.meta.MetaServerTest.MetaIoPackage.MetaArtPackage.MetaServerPackage.MetaTestPackage.MetaServicePackage.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.server.factory.ServiceMethodSpecificationFactory.*;
import static io.art.server.module.ServerActivator.*;
import static io.art.server.test.registry.TestServiceExecutionRegistry.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceMethodSpecificationTest {
    @BeforeAll
    public static void setup() {
        initialize(meta(MetaServerTest::new).getFactory(), logging().getFactory(), server().getFactory());
    }

    @Test
    public void testServiceMethodExecution() {
        MetaServerTest meta = library();
        MetaTestServiceClass serviceClass = meta.ioPackage().artPackage().serverPackage().testPackage().servicePackage().testServiceClass();

        forMethod(serviceClass, serviceClass.m1Method()).serve(Flux.just("test"));
        assertNotNull(executions().get("m1"), "m1");

        forMethod(serviceClass, serviceClass.m2Method()).serve(Flux.just("test"));
        assertEquals(executions().get("m2"), "test", "m2");

        forMethod(serviceClass, serviceClass.m3Method()).serve(Flux.just("test"));
        assertEquals(asMono(executions().get("m3")).block(), "test", "m3");

        forMethod(serviceClass, serviceClass.m4Method()).serve(Flux.just("test"));
        assertEquals(asFlux(executions().get("m4")).blockFirst(), "test", "m4");

        assertEquals(forMethod(serviceClass, serviceClass.m5Method()).serve(Flux.just("test")).blockFirst(), "test", "m5");
        assertNotNull(executions().get("m5"));
    }

}
