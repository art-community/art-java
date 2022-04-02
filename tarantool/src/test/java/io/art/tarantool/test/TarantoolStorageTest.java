package io.art.tarantool.test;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.logging.module.*;
import io.art.meta.model.*;
import io.art.meta.module.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.*;
import io.art.storage.service.*;
import io.art.tarantool.module.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import io.art.transport.module.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.model.Tuple.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.Tarantool.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolModule.tarantoolModule;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.manager.TestTarantoolInstanceManager.*;
import static io.art.tarantool.test.model.TestStorage.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class TarantoolStorageTest {
    @BeforeEach
    public void setup() {
        initializeStorage();
        initialize(
                MetaActivator.meta(() -> new MetaTarantoolTest(new MetaMetaTest())),
                TransportActivator.transport(),
                LoggingActivator.logging(),
                TarantoolActivator.tarantool(tarantool -> tarantool
                        .storages(storages -> storages
                                .storage(TestStorage.class, storage -> storage
                                        .connector(connector -> connector.client(client -> client
                                                .port(STORAGE_PORT)
                                                .username(USERNAME)
                                                .password(PASSWORD)))
                                        .space(TestingMetaModel.class, space -> space.indexes(TestModelIndexes.class))))
                        .subscriptions(subscriptions -> subscriptions.onService(TestService.class))
                )
        );
        tarantool()
                .schema(TestStorage.class)
                .createSpace(spaceFor(TestingMetaModel.class).ifNotExists(true).build())
                .createIndex(indexFor(testingMetaModel(), testModelIndexes().id())
                        .configure()
                        .ifNotExists(true)
                        .unique(true)
                        .build())
                .createIndex(indexFor(testingMetaModel(), testModelIndexes().f9f16())
                        .configure()
                        .ifNotExists(true)
                        .unique(false)
                        .build());
    }

    @AfterEach
    public void cleanup() {
        for (TarantoolStorageRegistry registry : tarantoolModule().configuration().getStorageRegistries().get().values()) {
            registry.getConnector().dispose();
        }
        shutdownStorage();
        shutdown();
    }

    @RepeatedTest(3)
    public void testSinglePut() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(current().put(data));
    }

    @RepeatedTest(3)
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

    @RepeatedTest(3)
    public void testSingleInsert() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(current().insert(data));
    }

    @RepeatedTest(3)
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

    @RepeatedTest(3)
    public void testSingleDelete() {
        TestingMetaModel data = generateTestingModel();
        current().insert(data);
        assertEquals(1, current().size());
        data.assertEquals(current().delete(data.getF1()));
        assertEquals(0, current().size());
    }

    @RepeatedTest(3)
    public void testMultipleDelete() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        current().delete(1, 2, 3);
        assertEquals(0, current().size());
    }

    @RepeatedTest(3)
    public void testSingleUpdate() {
        TestingMetaModel data = generateTestingModel().toBuilder().f33(fixedArrayOf("test")).f16("test").f9(10).build();
        current().insert(data);
        Integer f9 = data.getF9();
        assertEquals(f9 + 2, f9 = current().update(data.getF1(), updater -> updater.add(testingMetaModel().f9Field(), 2)).getF9());
        assertEquals(f9 - 2, f9 = current().update(data.getF1(), updater -> updater.subtract(testingMetaModel().f9Field(), 2)).getF9());
        assertEquals(f9 & 2, f9 = current().update(data.getF1(), updater -> updater.bitwiseAnd(testingMetaModel().f9Field(), 2)).getF9());
        assertEquals(f9 | 2, f9 = current().update(data.getF1(), updater -> updater.bitwiseOr(testingMetaModel().f9Field(), 2)).getF9());
        assertEquals(f9 ^ 2, current().update(data.getF1(), updater -> updater.bitwiseXor(testingMetaModel().f9Field(), 2)).getF9());
        assertEquals(2, current().update(data.getF1(), updater -> updater.set(testingMetaModel().f9Field(), 2)).getF9());
        assertEquals("t!!st", current().update(data.getF1(), updater -> updater.splice(testingMetaModel().f16Field(), 2, 1, "!!")).getF16());
        assertNull(current().update(data.getF1(), updater -> updater.delete(testingMetaModel().f33Field())).getF33());
    }

    @RepeatedTest(3)
    public void testUpsert() {
        Supplier<TestingMetaModel> generate = () -> generateTestingModel().toBuilder().f33(fixedArrayOf("test")).f9(10).build();
        TestingMetaModel data = generate.get();

        Integer f9 = data.getF9();

        current().upsert(generate.get(), updater -> updater.add(testingMetaModel().f9Field(), 2));
        data.assertEquals(current().first(data.getF1()));

        current().upsert(generate.get(), updater -> updater.add(testingMetaModel().f9Field(), 2));
        assertEquals(f9 + 2, f9 = current().first(data.getF1()).getF9());

        current().upsert(generate.get().toBuilder().build(), updater -> updater.subtract(testingMetaModel().f9Field(), 2));
        assertEquals(f9 - 2, f9 = current().first(data.getF1()).getF9());

        current().upsert(generate.get().toBuilder().build(), updater -> updater.bitwiseAnd(testingMetaModel().f9Field(), 2));
        assertEquals(f9 & 2, f9 = current().first(data.getF1()).getF9());

        current().upsert(generate.get(), updater -> updater.bitwiseOr(testingMetaModel().f9Field(), 2));
        assertEquals(f9 | 2, f9 = current().first(data.getF1()).getF9());

        current().upsert(generate.get(), updater -> updater.bitwiseXor(testingMetaModel().f9Field(), 2));
        assertEquals(f9 ^ 2, current().first(data.getF1()).getF9());

        current().upsert(generate.get(), updater -> updater.set(testingMetaModel().f9Field(), 2));
        assertEquals(2, current().first(data.getF1()).getF9());

        current().upsert(generate.get(), updater -> updater.delete(testingMetaModel().f33Field()));
        assertNull(current().first(data.getF1()).getF33());
    }

    @RepeatedTest(3)
    public void testMultipleUpdate() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f9(10).build(),
                generateTestingModel().toBuilder().f1(2).f9(10).build(),
                generateTestingModel().toBuilder().f1(3).f9(10).build()
        );
        current().insert(data);
        Integer expectedF9 = data.get(0).getF9();
        expectedF9 += 2;
        expectedF9 &= 2;
        expectedF9 |= 2;
        expectedF9 ^= 2;
        ImmutableArray<Integer> keys = immutableArrayOf(1, 2, 3);
        Integer finalExpectedF9 = expectedF9;
        current().update(keys, updater -> updater
                .add(testingMetaModel().f9Field(), 4)
                .set(testingMetaModel().f16Field(), "updated")
                .subtract(testingMetaModel().f9Field(), 2)
                .bitwiseAnd(testingMetaModel().f9Field(), 2)
                .bitwiseOr(testingMetaModel().f9Field(), 2)
                .bitwiseXor(testingMetaModel().f9Field(), 2))
                .stream()
                .peek(element -> assertEquals("updated", element.getF16()))
                .forEach(element -> assertEquals(finalExpectedF9, element.getF9()));
        current().update(keys, updater -> updater
                .set(testingMetaModel().f9Field(), 20)
                .delete(testingMetaModel().f33Field()))
                .stream()
                .peek(element -> assertEquals(20, element.getF9()))
                .forEach(element -> assertNull(element.getF33()));
    }

    @RepeatedTest(3)
    public void testTruncate() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        current().truncate();
        assertEquals(0, current().size());
    }

    @RepeatedTest(3)
    public void testCount() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        assertEquals(3, current().size());
        assertEquals(1, current().count(1));
    }

    @RepeatedTest(3)
    public void testFirst() {
        TestingMetaModel data = generateTestingModel();
        current().put(data);
        data.assertEquals(current().first(1));
    }

    @RepeatedTest(3)
    public void testSelect() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current().select(1);
        assertEquals(1, result.size());
        data.get(0).assertEquals(result.get(0));
    }

    @RepeatedTest(3)
    public void testFind() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current().find(1, 2, 3);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @RepeatedTest(3)
    public void testIndexCount() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        assertEquals(1, current().index(testModelIndexes().f9f16()).count(10, "test"));
    }

    @RepeatedTest(3)
    public void testIndexFirst() {
        TestingMetaModel data = generateTestingModel().toBuilder().f1(1).f9(10).f16("test").build();
        current().put(data);
        data.assertEquals(current().index(testModelIndexes().id()).first(1));
        data.assertEquals(current().index(testModelIndexes().f9f16()).first(10, "test"));
    }

    @RepeatedTest(3)
    public void testIndexSelect() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(3).f9(10).f16("test").build()
        );
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current().index(testModelIndexes().id()).select(1);
        assertEquals(1, result.size());
        data.get(0).assertEquals(result.get(0));

        result = current().index(testModelIndexes().f9f16()).select(10, "test", 1, 2);
        assertEquals(2, result.size());
        data.get(1).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
    }

    @RepeatedTest(3)
    public void testIndexFind() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(3).f9(10).f16("test").build()
        );
        current().put(data);
        ImmutableArray<TestingMetaModel> result = current().index(testModelIndexes().id()).find(1, 2, 3);
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));

        result = current().index(testModelIndexes().f9f16()).find(tuple(10, "test"));
        assertEquals(data.size(), result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));
    }

    @RepeatedTest(3)
    public void testIndexSingleDelete() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        TestingMetaModel result = current().index(testModelIndexes().f9f16()).delete(10, "test");
        data.get(0).assertEquals(result);
        assertEquals(2, current().size());

        result = current().index(testModelIndexes().id()).delete(2);
        data.get(1).assertEquals(result);
        assertEquals(1, current().size());
    }

    @RepeatedTest(3)
    public void testIndexMultipleDelete() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        ImmutableArray<TestingMetaModel> result = current().index(testModelIndexes().id()).delete(1, 2);
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        assertEquals(1, current().size());
    }

    @RepeatedTest(3)
    public void testIndexSingleUpdate() {
        TestingMetaModel data = generateTestingModel().toBuilder().f33(fixedArrayOf("test")).f10((short) 10).f9(10).f16("test").build();
        current().insert(data);
        short f10 = data.getF10();
        MetaField<MetaTestingMetaModelClass, Short> f10Field = testingMetaModel().f10Field();
        BlockingIndex2Service<TestingMetaModel, Integer, String> index = current().index(testModelIndexes().f9f16());
        assertEquals(f10 + 2, f10 = index.update(data.getF9(), data.getF16(), updater -> updater.add(f10Field, (short) 2)).getF10());
        assertEquals(f10 - 2, f10 = index.update(data.getF9(), data.getF16(), updater -> updater.subtract(f10Field, (short) 2)).getF10());
        assertEquals(f10 & 2, f10 = index.update(data.getF9(), data.getF16(), updater -> updater.bitwiseAnd(f10Field, (short) 2)).getF10());
        assertEquals(f10 | 2, f10 = index.update(data.getF9(), data.getF16(), updater -> updater.bitwiseOr(f10Field, (short) 2)).getF10());
        assertEquals(f10 ^ 2, (short) index.update(data.getF9(), data.getF16(), updater -> updater.bitwiseXor(f10Field, (short) 2)).getF10());
        assertEquals((short) 2, index.update(data.getF9(), data.getF16(), updater -> updater.set(f10Field, (short) 2)).getF10());
        assertNull(index.update(data.getF9(), data.getF16(), updater -> updater.delete(testingMetaModel().f33Field())).getF33());
    }

    @RepeatedTest(3)
    public void testIndexMultipleUpdate() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f10((short) 10).f33(fixedArrayOf("test")).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f10((short) 10).f33(fixedArrayOf("test")).f9(10).f16("test").build(),
                generateTestingModel().toBuilder().f1(3).f10((short) 10).f33(fixedArrayOf("test")).f9(10).f16("test").build()
        );
        current().insert(data);
        short expectedF10 = data.get(0).getF10();
        expectedF10 += 2;
        expectedF10 &= 2;
        expectedF10 |= 2;
        expectedF10 ^= 2;
        ImmutableArray<Tuple2<Integer, String>> keys = immutableArrayOf(tuple(10, "test"));
        short finalExpectedF10 = expectedF10;
        current().index(testModelIndexes().f9f16()).update(keys, updater -> updater
                .add(testingMetaModel().f10Field(), (short) 4)
                .set(testingMetaModel().f33Field(), linkedListOf("updated"))
                .subtract(testingMetaModel().f10Field(), (short) 2)
                .bitwiseAnd(testingMetaModel().f10Field(), (short) 2)
                .bitwiseOr(testingMetaModel().f10Field(), (short) 2)
                .bitwiseXor(testingMetaModel().f10Field(), (short) 2))
                .stream()
                .peek(element -> assertEquals("updated", element.getF33().get(0)))
                .forEach(element -> assertEquals(finalExpectedF10, element.getF10()));
        current().index(testModelIndexes().f9f16()).update(keys, updater -> updater
                .set(testingMetaModel().f10Field(), (short) 20)
                .delete(testingMetaModel().f33Field()))
                .stream()
                .peek(element -> assertEquals((short) 20, element.getF10()))
                .forEach(element -> assertNull(element.getF33()));
    }

    @RepeatedTest(3)
    public void testSubscription() {
        tarantool(TestStorage.class).testSubscription();
        assertTrue(TestService.await());
    }

    @RepeatedTest(3)
    public void testChannel() {
        CountDownLatch waiter = new CountDownLatch(2);
        tarantool(TestStorage.class).channel().testChannel().subscribe(value -> {
            assertEquals("test", value);
            waiter.countDown();
        });
        wrapExceptionCall(() -> waiter.await(30, TimeUnit.SECONDS), Assertions::fail);
    }

    private static BlockingSpaceService<Integer, TestingMetaModel> current() {
        return tarantool().space(TestStorage.class, TestingMetaModel.class);
    }
}
