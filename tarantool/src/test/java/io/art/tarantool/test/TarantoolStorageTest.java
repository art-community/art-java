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
        other().truncate();
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
    public void testStreamByNumbers() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current()
                .stream()
                .limit(2)
                .filter(filter -> filter.between(testingMetaModel().f1Field(), 1, 3))
                .sort(testingMetaModel().f1Field(), Sorter::descendant)
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
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current()
                .stream()
                .filter(filter -> filter.equal(testingMetaModel().f16Field(), data.get(1).getF16()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(filter -> filter.notEqual(testingMetaModel().f16Field(), data.get(1).getF16()))
                .collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = current()
                .stream()
                .filter(filter -> filter.in(testingMetaModel().f1Field(), data.get(0).getF1(), data.get(2).getF1()))
                .collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = current()
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
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current()
                .stream()
                .filter(filter -> filter.contains(testingMetaModel().f16Field(), data.get(1).getF16()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(filter -> filter.startsWith(testingMetaModel().f16Field(), "st"))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(filter -> filter.endsWith(testingMetaModel().f16Field(), "ng"))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));
    }

    @Test
    public void testStreamWithByNumbers() {
        List<TestingMetaModel> currentData = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f5(1).build(),
                generateTestingModel().toBuilder().f1(2).f5(2).build(),
                generateTestingModel().toBuilder().f1(3).f5(3).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, null, 3),
                new OtherSpace(2, null, 3),
                new OtherSpace(3, null, 3)
        );
        current().put(currentData);
        other().put(otherData);
        ImmutableArray<TestingMetaModel> result = current()
                .stream()
                .limit(2)
                .filter(otherSpace(), filter -> filter
                        .byKey(testingMetaModel().f5Field())
                        .lessThan(testingMetaModel().f1Field(), otherSpace().numberField()))
                .sort(testingMetaModel().f1Field(), Sorter::descendant)
                .collect();
        assertEquals(2, result.size());
        currentData.get(1).assertEquals(result.get(0));
        currentData.get(0).assertEquals(result.get(1));
    }

    @Test
    public void testStreamWithByStrings() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f5(1).f1(1).build(),
                generateTestingModel().toBuilder().f5(2).f1(2).f16("string").build(),
                generateTestingModel().toBuilder().f5(3).f1(3).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, "str", 3),
                new OtherSpace(2, "string", 3),
                new OtherSpace(3, "ng", 3)
        );
        current().put(data);
        other().put(otherData);
        ImmutableArray<TestingMetaModel> result = current()
                .stream()
                .filter(otherSpace(), filter -> filter
                        .byKey(testingMetaModel().f5Field())
                        .contains(testingMetaModel().f16Field(), otherSpace().valueField()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(otherSpace(), filter -> filter
                        .byKey(testingMetaModel().f5Field())
                        .endsWith(testingMetaModel().f16Field(), otherSpace().valueField()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(otherSpace(), filter -> filter
                        .byKey(testingMetaModel().f5Field())
                        .startsWith(testingMetaModel().f16Field(), otherSpace().valueField()))
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
        current().put(data);
        long result = current()
                .stream()
                .filter(filter -> filter.contains(testingMetaModel().f16Field(), data.get(1).getF16()))
                .count();
        assertEquals(1, result);

        boolean all = current()
                .stream()
                .filter(filter -> filter.between(testingMetaModel().f1Field(), 1, 2))
                .all(filter -> filter.equal(testingMetaModel().f6Field(), true));
        assertTrue(all);

        boolean any = current()
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

    private static SpaceService<Integer, TestingMetaModel> current() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }

    private static SpaceService<Integer, OtherSpace> other() {
        return Tarantool.tarantool().space(OtherSpace.class);
    }
}
