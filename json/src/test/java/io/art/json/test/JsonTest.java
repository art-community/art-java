package io.art.json.test;


import io.art.json.descriptor.*;
import io.art.json.module.*;
import io.art.json.test.model.*;
import meta.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.context.TestingContext.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.json.module.JsonModule.*;
import static io.art.json.test.generator.JsonTestModelGenerator.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import java.util.*;
import java.util.function.*;

public class JsonTest {
    @BeforeAll
    public static void setup() {
        testing(
                meta(MetaJsonTest::new).getFactory(),
                JsonModule::new
        );
    }

    @Test
    public void testJsonRead() {
        JsonModelWriter writer = new JsonModelWriter(jsonModule().configuration().getObjectMapper().getFactory());
        JsonModelReader reader = new JsonModelReader(jsonModule().configuration().getObjectMapper().getFactory());
        Model model = generateModel();
        String json = writer.writeToString(typed(declaration(Model.class).definition(), model));
        System.out.println("JSON: " + json);
        Assertions.assertThat(reader.read(declaration(Model.class).definition(), json))
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes("stream", ".+mono.+")
                .withEqualsForType((current, other) -> Objects.equals(((Supplier<?>) current).get(), ((Supplier<?>) other).get()), Supplier.class)
                .withEqualsForType((current, other) -> Objects.equals(dynamicArrayOf(((Flux<?>) current).toIterable()), dynamicArrayOf(((Flux<?>) other).toIterable())), Flux.class)
                .isEqualTo(model);
    }

}
