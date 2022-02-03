package io.art.tarantool.test.space;

import io.art.core.collection.*;
import io.art.tarantool.*;
import io.art.tarantool.model.*;
import io.art.tarantool.service.*;
import io.art.tarantool.test.factory.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.FieldType.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.factory.TarantoolTestDataFactory.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.*;

public class TarantoolTest {
    private static TarantoolSpaceService<Integer, TestData> space;

    @BeforeAll
    public static void setup() {
        initialize(logging(),
                meta(MetaTarantoolTest::new),
                transport(),
                tarantool(tarantool -> tarantool
                        .storage(TestStorage.class, storage -> storage.client(client -> client.username("username").password("password")))
                        .space(TestStorage.class, TestData.class)
                )
        );
        space = Tarantool.tarantool().space(TestData.class);
        TarantoolSchemaService schema = Tarantool.tarantool().schema(TestStorage.class);
        schema.createSpace(spaceFor(TestData.class).ifNotExists(true).build());
        schema.createIndex(indexFor(TestData.class, MetaTestDataClass::idField)
                .ifNotExists(true)
                .unique(true)
                .part(TarantoolIndexPartConfiguration.builder()
                        .field(1)
                        .type(UNSIGNED)
                        .build())
                .build());
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }

    @Test
    public void testSinglePut() {
        TestData data = testData(1);
        assertEquals(data, space.put(data));
    }

    @Test
    public void testFindFirst() {
        TestData data = testData(1);
        space.put(data);
        assertEquals(data, space.findFirst(1));
    }

    @Test
    public void testFindAll() {
        space.put(IntStream.range(1, 1_000_000).boxed().map(TarantoolTestDataFactory::testData).collect(ImmutableArray.immutableArrayCollector()));
    }
}
