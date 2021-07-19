package io.art.json.test;


import io.art.json.descriptor.*;
import io.art.json.module.*;
import io.art.json.test.model.*;
import meta.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.context.TestingContextFactory.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.json.module.JsonModule.*;
import static io.art.json.test.generator.ModelGenerator.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import static org.assertj.core.api.Assertions.*;
import java.time.*;
import java.time.chrono.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

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
        JsonModelWriter writer = jsonModule().configuration().getWriter();
        JsonModelReader reader = jsonModule().configuration().getReader();
        Model model = generateModel();
        String json = writer.writeToString(typed(declaration(Model.class).definition(), model));
        assertThat(reader.read(declaration(Model.class).definition(), json))
                .usingRecursiveComparison()
                .withEqualsForType((current, other) -> Objects.equals(((Supplier<?>) current).get(), ((Supplier<?>) other).get()), Supplier.class)
                .withEqualsForType((current, other) -> Objects.equals(dynamicArrayOf(((Flux<?>) current).toIterable()), dynamicArrayOf(((Flux<?>) other).toIterable())), Flux.class)
                .withEqualsForType(ChronoZonedDateTime::isEqual, ZonedDateTime.class)
                .withEqualsForType((current, other) -> nonNull(other), Flux.class)
                .withEqualsForType((current, other) -> nonNull(other), Mono.class)
                .withEqualsForType((current, other) -> nonNull(other), Stream.class)
                .isEqualTo(model);
    }

}
