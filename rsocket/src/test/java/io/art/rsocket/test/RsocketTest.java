package io.art.rsocket.test;

import io.art.core.extensions.*;
import io.art.core.property.*;
import io.art.meta.test.meta.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.meta.MetaRsocketTest.MetaIoPackage.MetaArtPackage.MetaRsocketPackage.MetaTestPackage.MetaServicePackage.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.art.server.factory.ServiceMethodFactory.*;

public class RsocketTest {
    @BeforeAll
    public static void setup() {
        LazyProperty<MetaTestRsocketServiceClass> declaration = lazy(() -> cast(declaration(TestRsocketService.class)));
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                json(),
                rsocket(rsocket -> rsocket
                        .register(() -> preconfiguredServiceMethod(declaration.get(), declaration.get().mMethod()))
                        .activateServer()
                        .serverLogging(true))
        );
    }

    @Test
    public void test() {
        ThreadExtensions.block();
    }
}
