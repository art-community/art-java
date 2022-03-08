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
import static io.art.core.wrapper.ExceptionWrapper.*;
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
import java.util.concurrent.*;

public class TarantoolStorageTest {
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
        shutdownStorage();
        shutdown();
    }

    @AfterEach
    public void truncate() {
        current().truncate();
    }

    @Test
    public void testSinglePut() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(current().put(data));
    }

    @Test
    public void testMultiplePut() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        ImmutableArray<TestingMetaModel> result = current().put(data);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @Test
    public void testSingleInsert() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(current().insert(data));
    }

    @Test
    public void testMultipleInsert() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        ImmutableArray<TestingMetaModel> result = current().insert(data);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @Test
    public void testSingleDelete() {
        TestingMetaModel data = generateTestingModel();
        current().insert(data);
        assertEquals(1, current().count());
        data.assertEquals(current().delete(data.getF1()));
        assertEquals(0, current().count());
    }

    @Test
    public void testMultipleDelete() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        current().delete(1, 2, 3);
        assertEquals(0, current().count());
    }

    @Test
    public void testTruncate() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        current().truncate();
        assertEquals(0, current().count());
    }

    @Test
    public void testCount() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        assertEquals(3, current().count());
    }

    @Test
    public void testFindFirst() {
        TestingMetaModel data = generateTestingModel();
        current().put(data);
        data.assertEquals(current().findFirst(1));
    }

    @Test
    public void testFindAll() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current().findAll(1, 2, 3);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @Test
    public void testSubscription() {
        Tarantool.tarantool(TestStorage.class).testSubscription();
        assertTrue(TestService.await());
    }

    @Test
    public void testChannel() {
        CountDownLatch waiter = new CountDownLatch(2);
        Tarantool.tarantool(TestStorage.class).channel().testChannel().subscribe(value -> {
            assertEquals("test", value);
            waiter.countDown();
        });
        wrapExceptionCall(() -> waiter.await(30, TimeUnit.SECONDS), Assertions::fail);
    }

    private static SpaceService<Integer, TestingMetaModel> current() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }
}
