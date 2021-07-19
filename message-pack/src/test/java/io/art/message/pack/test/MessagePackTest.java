package io.art.message.pack.test;


import io.art.core.collection.*;
import io.art.message.pack.descriptor.*;
import io.art.message.pack.module.*;
import io.art.message.pack.test.model.*;
import meta.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.context.TestingContextFactory.*;
import static io.art.message.pack.module.MessagePackModule.*;
import static io.art.message.pack.test.generator.ModelGenerator.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;
import static java.util.Objects.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import java.time.*;
import java.time.chrono.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class MessagePackTest {
    @BeforeAll
    public static void setup() {
        testing(
                meta(MetaMessagePackTest::new).getFactory(),
                MessagePackModule::new
        );
    }

    @Test
    public void testMessagePack() {
        MessagePackModelWriter writer = messagePackModule().configuration().getWriter();
        MessagePackModelReader reader = messagePackModule().configuration().getReader();
        Model model = generateModel();
        byte[] bytes = writer.writeToBytes(typed(declaration(Model.class).definition(), model));
        assertThat(reader.read(declaration(Model.class).definition(), bytes))
                .usingRecursiveComparison()
                .withEqualsForType((current, other) -> Objects.equals(((Supplier<?>) current).get(), ((Supplier<?>) other).get()), Supplier.class)
                .withEqualsForType(ChronoZonedDateTime::isEqual, ZonedDateTime.class)
                .withEqualsForType(Object::equals, ImmutableMap.class)
                .withEqualsForType((current, other) -> nonNull(other), Flux.class)
                .withEqualsForType((current, other) -> nonNull(other), Mono.class)
                .withEqualsForType((current, other) -> nonNull(other), Stream.class)
                .isEqualTo(model);
    }

}
