package io.art.tarantool.test;

import io.art.core.collection.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.*;
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
    }

    @AfterAll
    public static void cleanup() {
        shutdownStorage();
        shutdown();
    }

    @AfterEach
    public void truncate() {
        space().truncate();
    }

    @Test
    public void testSinglePut() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(space().put(data));
    }

    @Test
    public void testMultiplePut() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        ImmutableArray<TestingMetaModel> result = space().put(data);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @Test
    public void testSingleInsert() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(space().insert(data));
    }

    @Test
    public void testMultipleInsert() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        ImmutableArray<TestingMetaModel> result = space().insert(data);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @Test
    public void testSingleDelete() {
        TestingMetaModel data = generateTestingModel();
        space().insert(data);
        assertEquals(1, space().count());
        data.assertEquals(space().delete(data.getF1()));
        assertEquals(0, space().count());
    }

    @Test
    public void testMultipleDelete() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().insert(data);
        space().delete(1, 2, 3);
        assertEquals(0, space().count());
    }

    @Test
    public void testTruncate() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().insert(data);
        space().truncate();
        assertEquals(0, space().count());
    }

    @Test
    public void testCount() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().insert(data);
        assertEquals(3, space().count());
    }

    @Test
    public void testFindFirst() {
        TestingMetaModel data = generateTestingModel();
        space().put(data);
        data.assertEquals(space().findFirst(1));
    }

    @Test
    public void testFindAll() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().put(data);
        ImmutableArray<TestingMetaModel> result = space().findAll(1, 2, 3);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @Test
    public void testStreamByNumbers() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().put(data);
        ImmutableArray<TestingMetaModel> result = space()
                .stream()
                .limit(2)
                .filter(filter -> filter.between(testingMetaModel().f1Field(), 1, 3))
                .sort(testingMetaModel().f1Field(), SpaceStream.Sorter::descendant)
                .collect();
        assertEquals(2, result.size());
        data.get(1).assertEquals(result.get(0));
        data.get(0).assertEquals(result.get(1));
    }

    @Test
    public void testStreamByValues() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("string").build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().put(data);
        ImmutableArray<TestingMetaModel> result = space()
                .stream()
                .filter(filter -> filter.equal(testingMetaModel().f16Field(), data.get(1).getF16()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = space()
                .stream()
                .filter(filter -> filter.notEqual(testingMetaModel().f16Field(), data.get(1).getF16()))
                .collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = space()
                .stream()
                .filter(filter -> filter.in(testingMetaModel().f1Field(), data.get(0).getF1(), data.get(2).getF1()))
                .collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = space()
                .stream()
                .filter(filter -> filter.notIn(testingMetaModel().f1Field(), data.get(0).getF1(), data.get(2).getF1()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));
    }

    @Test
    public void testStreamByStrings() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("string").build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        space().put(data);
        ImmutableArray<TestingMetaModel> result = space()
                .stream()
                .filter(filter -> filter.contains(testingMetaModel().f16Field(), data.get(1).getF16()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = space()
                .stream()
                .filter(filter -> filter.startsWith(testingMetaModel().f16Field(), "st"))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = space()
                .stream()
                .filter(filter -> filter.endsWith(testingMetaModel().f16Field(), "ng"))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));
    }

    @Test
    public void testStreamTerminatingOperations() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f6(true).build(),
                generateTestingModel().toBuilder().f1(2).f6(true).f16("string").build(),
                generateTestingModel().toBuilder().f1(3).f6(false).build()
        );
        space().put(data);
        long result = space()
                .stream()
                .filter(filter -> filter.contains(testingMetaModel().f16Field(), data.get(1).getF16()))
                .count();
        assertEquals(1, result);

        boolean all = space()
                .stream()
                .filter(filter -> filter.between(testingMetaModel().f1Field(), 1, 2))
                .all(filter -> filter.equal(testingMetaModel().f6Field(), true));
        assertTrue(all);

        boolean any = space()
                .stream()
                .any(filter -> filter.startsWith(testingMetaModel().f16Field(), "st"));

        assertTrue(any);
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

    private static SpaceService<Integer, TestingMetaModel> space() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }
}
