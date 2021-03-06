package io.art.json.test;


import io.art.json.descriptor.*;
import io.art.json.module.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.TestingContextFactory.*;
import static io.art.json.module.JsonModule.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;

public class JsonTest {
    @BeforeAll
    public static void setup() {
        testing(meta(MetaMetaTest::new).getFactory(), JsonModule::new);
    }

    @Test
    public void testJson() {
        JsonModelWriter writer = jsonModule().configuration().getWriter();
        JsonModelReader reader = jsonModule().configuration().getReader();
        TestingMetaModel model = generateModel();
        String json = writer.writeToString(typed(declaration(TestingMetaModel.class).definition(), model));
        TestingMetaModel parsed = reader.read(declaration(TestingMetaModel.class).definition(), json);
        parsed.assertEquals(model);
    }
}
