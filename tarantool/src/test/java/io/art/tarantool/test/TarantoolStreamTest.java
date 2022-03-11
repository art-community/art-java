package io.art.tarantool.test;

import io.art.core.collection.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.service.*;
import io.art.tarantool.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.manager.TestTarantoolInstanceManager.*;
import static io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.MetaOtherSpaceClass.*;
import static io.art.transport.module.TransportActivator.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TarantoolStreamTest {
    @BeforeAll
    public static void setup() {
        initializeStorage();
        initialize(
                meta(() -> new MetaTarantoolTest(new MetaMetaTest())),
                transport(),
                tarantool(tarantool -> tarantool
                        .storage(TestStorage.class, storage -> storage.client(client -> client
                                .port(STORAGE_PORT)
                                .username(USERNAME)
                                .password(PASSWORD)))
                        .subscribe(subscriptions -> subscriptions.onService(TestService.class))
                        .space(TestStorage.class, TestingMetaModel.class, () -> testingMetaModel().f1Field())
                        .space(TestStorage.class, OtherSpace.class, () -> otherSpace().keyField())
                )
        );
        Tarantool.tarantool()
                .schema(TestStorage.class)
                .createSpace(spaceFor(TestingMetaModel.class).ifNotExists(true).build())
                .createIndex(indexFor(testingMetaModel())
                        .field(testingMetaModel().f1Field())
                        .configure()
                        .ifNotExists(true)
                        .unique(true)
                        .build());
        Tarantool.tarantool()
                .schema(TestStorage.class)
                .createSpace(spaceFor(OtherSpace.class).ifNotExists(true).build())
                .createIndex(indexFor(otherSpace())
                        .field(otherSpace().keyField())
                        .configure()
                        .ifNotExists(true)
                        .unique(true)
                        .build());
    }

    @AfterAll
    public static void cleanup() {
        //shutdownStorage();
        shutdown();
    }

    @AfterEach
    public void truncate() {
        current().truncate();
        other().truncate();
    }

    @Test
    public void testAny() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        assertTrue(current().stream().any(filter -> filter.byNumber(testingMetaModel().f1Field()).equal(2)));
    }

    @Test
    public void testAll() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build()
        );
        current().insert(data);
        assertTrue(current().stream().all(filter -> filter.byString(testingMetaModel().f16Field()).equal("test")));
    }

    @Test
    public void testNone() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build()
        );
        current().insert(data);
        assertTrue(current().stream().none(filter -> filter.byString(testingMetaModel().f16Field()).equal("string")));
    }

    @Test
    public void testRange() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build(),
                generateTestingModel().toBuilder().f1(4).f16("test").build()
        );
        current().insert(data);
        ImmutableArray<TestingMetaModel> result = current().stream().range(1, 2).collect();
        assertEquals(2, result.size());
        data.get(1).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
    }

    @Test
    public void testDistinct() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").build()
        );
        current().insert(data);
        ImmutableArray<TestingMetaModel> result = current().stream().distinct(testingMetaModel().f16Field()).collect();
        assertEquals(2, result.size());
        data.get(2).assertEquals(result.get(0));
        data.get(3).assertEquals(result.get(1));
    }

    @Test
    public void testDistinct() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").build()
        );
        current().insert(data);
        ImmutableArray<TestingMetaModel> result = current().stream().distinct(testingMetaModel().f16Field()).collect();
        assertEquals(2, result.size());
        data.get(2).assertEquals(result.get(0));
        data.get(3).assertEquals(result.get(1));
    }

    private static SpaceService<Integer, TestingMetaModel> current() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }

    private static SpaceService<Integer, OtherSpace> other() {
        return Tarantool.tarantool().space(OtherSpace.class);
    }
}
