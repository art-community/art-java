package io.art.json.test;


import io.art.json.descriptor.*;
import io.art.meta.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import org.junit.jupiter.api.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.json.module.JsonModule.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;

public class JsonTest {
    @BeforeAll
    public static void setup() {
        initialize(meta(MetaMetaTest::new), json());
    }

    @Test
    public void testJson() {
        JsonWriter writer = jsonModule().configuration().getWriter();
        JsonReader reader = jsonModule().configuration().getReader();
        TestingMetaModel model = generateTestingModel();
        String json = writer.writeToString(typed(Meta.declaration(TestingMetaModel.class).definition(), model));
        TestingMetaModel parsed = reader.read(Meta.declaration(TestingMetaModel.class).definition(), json);
        parsed.assertEquals(model);
    }
}
