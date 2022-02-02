package io.art.tarantool.test.space;

import io.art.tarantool.*;
import io.art.tarantool.model.*;
import io.art.tarantool.service.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.factory.TarantoolTestDataFactory.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TarantoolTest {
    private static TarantoolSpaceService<Integer, TestData> space;
    private static TarantoolSchemaService schema;

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
        schema = Tarantool.tarantool().schema(TestStorage.class);
        schema.createSpace(TarantoolSpaceConfiguration.builder()
                .name(idByDash(TestData.class))
                .ifNotExists(true)
                .build());
        schema.createIndex(TarantoolIndexConfiguration.builder()
                .spaceName(idByDash(TestData.class))
                .indexName("primary")
                .ifNotExists(true)
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
        List<TestData> data = fixedArrayOf(testData(2), testData(3), testData(4));
        space.put(data);
        assertEquals(data, space.findAll(2, 3, 4).toMutable());
    }
}
