package io.art.yaml.test;


import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.yaml.descriptor.*;
import io.art.yaml.module.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.TestingContextFactory.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;
import static io.art.yaml.module.YamlModule.*;

public class YamlTest {
    @BeforeAll
    public static void setup() {
        testing(meta(MetaMetaTest::new).getFactory(), YamlModule::new);
    }

    @Test
    public void testJsonRead() {
        YamlModelWriter writer = yamlModule().configuration().getWriter();
        YamlModelReader reader = yamlModule().configuration().getReader();
        TestingMetaModel model = generateModel();
        String json = writer.writeToString(typed(declaration(TestingMetaModel.class).definition(), model));
        TestingMetaModel parsed = reader.read(declaration(TestingMetaModel.class).definition(), json);
        parsed.assertEquals(model);
    }
}
