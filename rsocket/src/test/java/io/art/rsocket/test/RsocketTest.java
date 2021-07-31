package io.art.rsocket.test;

import io.art.core.extensions.*;
import io.art.meta.test.meta.*;
import io.art.rsocket.test.meta.*;
import io.art.rsocket.test.service.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.module.RsocketActivator.*;

public class RsocketTest {
    @BeforeAll
    public static void setup() {
        initialize(
                meta(() -> new MetaRsocketTest(new MetaMetaTest())),
                logging(),
                json(),
                rsocket(rsocket -> rsocket.server(server -> server.logging().register(TestRsocketService.class)))
        );
    }

    @Test
    public void test() {
        ThreadExtensions.block();
    }
}
