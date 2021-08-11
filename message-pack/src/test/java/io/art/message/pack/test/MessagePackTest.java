package io.art.message.pack.test;


import io.art.message.pack.descriptor.*;
import io.art.meta.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.message.pack.module.MessagePackActivator.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;

public class MessagePackTest {
    @BeforeAll
    public static void setup() {
        initialize(meta(MetaMetaTest::new), messagePack());
    }

    @Test
    public void testMessagePack() {
        MessagePackWriter writer = messagePackModule().configuration().getWriter();
        MessagePackReader reader = messagePackModule().configuration().getReader();
        TestingMetaModel model = generateTestingModel();
        byte[] bytes = writer.writeToBytes(typed(Meta.declaration(TestingMetaModel.class).definition(), model));
        TestingMetaModel parsed = reader.read(Meta.declaration(TestingMetaModel.class).definition(), bytes);
        parsed.assertEquals(model);
    }

}
