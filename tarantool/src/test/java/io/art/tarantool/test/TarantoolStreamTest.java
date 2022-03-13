package io.art.tarantool.test;

import io.art.core.collection.*;
import io.art.meta.module.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.service.*;
import io.art.storage.sorter.model.*;
import io.art.tarantool.module.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import io.art.transport.module.*;
import org.junit.jupiter.api.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.Tarantool.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.manager.TestTarantoolInstanceManager.*;
import static io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.MetaOtherSpaceClass.*;
import static io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.MetaTestStorageClass.*;
import static io.art.tarantool.test.model.TestStorage.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TarantoolStreamTest {
    @BeforeAll
    public static void setup() {
        initializeStorage();
        initialize(
                MetaActivator.meta(() -> new MetaTarantoolTest(new MetaMetaTest())),
                TransportActivator.transport(),
                TarantoolActivator.tarantool(tarantool -> tarantool
                        .storage(TestStorage.class, storage -> storage.client(client -> client
                                .port(STORAGE_PORT)
                                .username(USERNAME)
                                .password(PASSWORD)))
                        .subscribe(subscriptions -> subscriptions.onService(TestService.class))
                        .space(TestStorage.class, TestingMetaModel.class, TestModelIndexes.class)
                        .space(TestStorage.class, OtherSpace.class, OtherSpaceIndexes.class)
                )
        );
        tarantool()
                .schema(TestStorage.class)
                .createSpace(spaceFor(TestingMetaModel.class).ifNotExists(true).build())
                .createIndex(indexFor(testingMetaModel())
                        .field(testingMetaModel().f1Field())
                        .configure()
                        .ifNotExists(true)
                        .unique(true)
                        .build())
                .createSpace(spaceFor(OtherSpace.class).ifNotExists(true).build())
                .createIndex(indexFor(otherSpace())
                        .field(otherSpace().keyField())
                        .configure()
                        .ifNotExists(true)
                        .unique(true)
                        .build())
                .createIndex(indexFor(otherSpace())
                        .field(otherSpace().valueField())
                        .field(otherSpace().numberField())
                        .configure()
                        .ifNotExists(true)
                        .unique(false)
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
    public void testCount() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).build(),
                generateTestingModel().toBuilder().f1(2).build(),
                generateTestingModel().toBuilder().f1(3).build()
        );
        current().insert(data);
        assertEquals(3, current().stream().count());
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
    public void testSort() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").build()
        );
        current().insert(data);
        ImmutableArray<TestingMetaModel> result = current().stream().sort(testingMetaModel().f1Field(), Sorter::descendant).collect();
        data.get(3).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
        data.get(1).assertEquals(result.get(2));
        data.get(0).assertEquals(result.get(3));
    }

    @Test
    public void testMax() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").build()
        );
        current().insert(data);
        TestingMetaModel result = current().stream().max(testingMetaModel().f1Field()).orElseThrow(AssertionError::new);
        data.get(3).assertEquals(result);
    }

    @Test
    public void testMin() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").build(),
                generateTestingModel().toBuilder().f1(3).f16("test").build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").build()
        );
        current().insert(data);
        TestingMetaModel result = current().stream().min(testingMetaModel().f1Field()).orElseThrow(AssertionError::new);
        data.get(0).assertEquals(result);
    }

    @Test
    public void testMapJoinField() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(2).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").f9(3).build()
        );
        current().insert(data);
        ImmutableArray<String> result = current().stream().map(testingMetaModel().f16Field()).collect(immutableArrayCollector());
        assertEquals(4, result.size());
        assertEquals("test", result.get(0));
        assertEquals("test 2", result.get(1));
        assertEquals("test", result.get(2));
        assertEquals("test 2", result.get(3));
    }

    @Test
    public void testMapJoinKey() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(2).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").f9(3).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, "test", 1),
                new OtherSpace(2, "test", 2)
        );
        current().insert(data);
        other().insert(otherData);
        ImmutableArray<OtherSpace> result = current().stream().map(otherSpace(), testingMetaModel().f9Field()).collect();
        assertEquals(3, result.size());
        assertEquals(otherData.get(0), result.get(0));
        assertEquals(otherData.get(0), result.get(1));
        assertEquals(otherData.get(1), result.get(2));
    }

    @Test
    public void testMapJoinIndex() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(2).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").f9(3).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, "test", 1),
                new OtherSpace(2, "test", 2)
        );
        current().insert(data);
        other().insert(otherData);
        ImmutableArray<OtherSpace> result = current()
                .stream()
                .map(otherSpaceIndexes().valueNumber(), testingMetaModel().f16Field(), testingMetaModel().f9Field())
                .collect();
        assertEquals(2, result.size());
        assertEquals(otherData.get(0), result.get(0));
        assertEquals(otherData.get(1), result.get(1));
    }

    @Test
    public void testMapJoinFunction() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(2).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 2").f9(3).build()
        );
        current().insert(data);
        ImmutableArray<String> result = current().stream().map(testStorage().testMapperMethod()).collect(immutableArrayCollector());
        assertEquals(4, result.size());
        assertEquals("test - mapped", result.get(0));
        assertEquals("test 2 - mapped", result.get(1));
        assertEquals("test - mapped", result.get(2));
        assertEquals("test 2 - mapped", result.get(3));
    }

    @Test
    public void testFilterField() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("started").f9(2).build(),
                generateTestingModel().toBuilder().f1(2).f16("test").f9(4).build(),
                generateTestingModel().toBuilder().f1(3).f16("end").f9(6).build()
        );
        current().insert(data);

        ImmutableArray<TestingMetaModel> result = current().stream().filter(filter -> filter
                .byString(testingMetaModel().f16Field())
                .startsWith("st"))
                .collect();
        assertEquals(1, result.size());
        data.get(0).assertEquals(result.get(0));

        result = current().stream().filter(filter -> filter.byString(testingMetaModel().f16Field()).endsWith("nd")).collect();
        assertEquals(1, result.size());
        data.get(2).assertEquals(result.get(0));

        result = current().stream().filter(filter -> filter.byString(testingMetaModel().f16Field()).contains("es")).collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current().stream().filter(filter -> filter.byString(testingMetaModel().f16Field()).equal("test")).collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current().stream().filter(filter -> filter.byString(testingMetaModel().f16Field()).notEqual("test")).collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = current().stream().filter(filter -> filter.byString(testingMetaModel().f16Field()).in("started", "test")).collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));

        result = current().stream().filter(filter -> filter.byString(testingMetaModel().f16Field()).notIn("started", "test")).collect();
        assertEquals(1, result.size());
        data.get(2).assertEquals(result.get(0));

        result = current().stream().filter(filter -> filter.byNumber(testingMetaModel().f9Field()).lessThan(5)).collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));

        result = current().stream().filter(filter -> filter.byNumber(testingMetaModel().f9Field()).lessThanEqual(6)).collect();
        assertEquals(3, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));

        result = current().stream().filter(filter -> filter.byNumber(testingMetaModel().f9Field()).moreThan(2)).collect();
        assertEquals(2, result.size());
        data.get(1).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = current().stream().filter(filter -> filter.byNumber(testingMetaModel().f9Field()).moreThanEquals(1)).collect();
        assertEquals(3, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(1).assertEquals(result.get(1));
        data.get(2).assertEquals(result.get(2));

        result = current().stream().filter(filter -> filter.byNumber(testingMetaModel().f9Field()).between(3, 5)).collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current().stream().filter(filter -> filter.byNumber(testingMetaModel().f9Field()).notBetween(3, 5)).collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
    }

    @Test
    public void testFilterFunction() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("started").f9(2).build(),
                generateTestingModel().toBuilder().f1(2).f16("test").f9(4).build(),
                generateTestingModel().toBuilder().f1(3).f16("end").f9(6).build()
        );
        current().insert(data);

        ImmutableArray<TestingMetaModel> result = current()
                .stream()
                .filter(filter -> filter.byFunction(testStorage().testFilterMethod()))
                .collect();
        assertEquals(2, result.size());
        data.get(1).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
    }

    @Test
    public void testFilterJoinKey() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 3").f9(2).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, "test", 1),
                new OtherSpace(2, "test 3", 2)
        );
        current().insert(data);
        other().insert(otherData);
        ImmutableArray<TestingMetaModel> result = current().stream()
                .filter(filter -> filter
                        .bySpace(otherSpace(), testingMetaModel().f9Field())
                        .currentString(testingMetaModel().f16Field())
                        .equal(otherSpace().valueField()))
                .collect();
        assertEquals(3, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
        data.get(3).assertEquals(result.get(2));

        result = current().stream()
                .filter(filter -> filter
                        .bySpace(otherSpace(), testingMetaModel().f9Field())
                        .otherField(otherSpace().valueField())
                        .equal("test 3"))
                .collect();
        assertEquals(1, result.size());
        data.get(3).assertEquals(result.get(0));
    }

    @Test
    public void testFilterJoinIndex() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 3").f9(2).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, "test", 1),
                new OtherSpace(2, "test 3", 2)
        );
        current().insert(data);
        other().insert(otherData);
        ImmutableArray<TestingMetaModel> result = current().stream()
                .filter(filter -> filter
                        .byIndex(otherSpaceIndexes().valueNumber(), testingMetaModel().f16Field(), testingMetaModel().f9Field())
                        .currentString(testingMetaModel().f16Field())
                        .equal(otherSpace().valueField()))
                .collect();
        assertEquals(3, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
        data.get(3).assertEquals(result.get(2));

        result = current().stream()
                .filter(filter -> filter
                        .byIndex(otherSpaceIndexes().valueNumber(), testingMetaModel().f16Field(), testingMetaModel().f9Field())
                        .otherField(otherSpace().valueField())
                        .equal("test 3"))
                .collect();
        assertEquals(1, result.size());
        data.get(3).assertEquals(result.get(0));
    }

    @Test
    public void testFilterJoinKeyConditioned() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(2).f16("test 2").f9(1).build(),
                generateTestingModel().toBuilder().f1(3).f16("test").f9(1).build(),
                generateTestingModel().toBuilder().f1(4).f16("test 3").f9(2).build(),
                generateTestingModel().toBuilder().f1(5).f16("any").f9(3).build()
        );
        List<OtherSpace> otherData = fixedArrayOf(
                new OtherSpace(1, "test", 1),
                new OtherSpace(2, "test 3", 2),
                new OtherSpace(3, "test 4", 3),
                new OtherSpace(4, "test 4", 4)
        );
        current().insert(data);
        other().insert(otherData);
        ImmutableArray<TestingMetaModel> result = current().stream()
                .filter(filter -> filter
                        .bySpace(otherSpace(), testingMetaModel().f9Field())
                        .currentString(testingMetaModel().f16Field())
                        .equal(otherSpace().valueField())

                        .or(nested -> nested
                                .bySpace(otherSpace(), testingMetaModel().f9Field())
                                .otherField(otherSpace().valueField())
                                .equal("test 4")

                                .and()
                                .bySpace(otherSpace(), testingMetaModel().f9Field())
                                .otherField(otherSpace().numberField())
                                .equal(3)
                        )
                )

                .collect();

        assertEquals(4, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
        data.get(3).assertEquals(result.get(2));
        data.get(4).assertEquals(result.get(3));
    }

    @Test
    public void testBaseKey() {
        List<TestingMetaModel> data = fixedArrayOf(
                generateTestingModel().toBuilder().f1(1).f16("not base").f9(2).build(),
                generateTestingModel().toBuilder().f1(2).f16("base").f9(4).build(),
                generateTestingModel().toBuilder().f1(3).f16("base").f9(6).build()
        );
        current().insert(data);

        ImmutableArray<TestingMetaModel> result = current().stream(2).collect();
        assertEquals(2, result.size());
        data.get(1).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));
    }

    private static SpaceService<Integer, TestingMetaModel> current() {
        return tarantool().space(TestingMetaModel.class);
    }

    private static SpaceService<Integer, OtherSpace> other() {
        return tarantool().space(OtherSpace.class);
    }

}
