package io.art.tarantool.test;

import io.art.tarantool.*;
import io.art.tarantool.model.*;
import io.art.tarantool.service.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.network.selector.PortSelector.SocketType.TCP;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.FieldType.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.factory.TarantoolTestDataFactory.*;
import static io.art.tarantool.test.runner.TestTarantoolRunner.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TarantoolStorageTest {
    private static TarantoolSpaceService<Integer, TestData> space;

    @BeforeAll
    public static void setup() {
        runStorage();
        initialize(logging(),
                meta(MetaTarantoolTest::new),
                transport(),
                tarantool(tarantool -> tarantool
                        .storage(TestStorage.class, storage -> storage.client(client -> client
                                .port(STORAGE_PORT)
                                .username(USERNAME)
                                .password(PASSWORD)))
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
        deleteScripts();
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
