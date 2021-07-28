package io.art.rsocket.test;

import io.art.meta.test.meta.*;
import io.art.rsocket.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.rsocket.module.RsocketActivator.*;

public class RsocketTest {
    @BeforeAll
    public void setup() {
        initialize(meta(() -> new MetaRsocket(new MetaMetaTest())), logging(), rsocket(), json());
    }

    @Test
    public void test() {

    }

}
