package io.art.message.pack.test;


import io.art.core.initializer.*;
import io.art.message.pack.descriptor.*;
import io.art.message.pack.module.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;

public class MessagePackTest {
    @BeforeAll
    public static void setup() {
        ContextInitializer.initialize(meta(MetaMetaTest::new).getFactory(), MessagePackModule::new);
    }

    @Test
    public void testMessagePack() {
        MessagePackModelWriter writer = messagePackModule().configuration().getWriter();
        MessagePackModelReader reader = messagePackModule().configuration().getReader();
        TestingMetaModel model = generateTestingModel();
        byte[] bytes = writer.writeToBytes(typed(declaration(TestingMetaModel.class).definition(), model));
        TestingMetaModel parsed = reader.read(declaration(TestingMetaModel.class).definition(), bytes);
        parsed.assertEquals(model);
    }

}
