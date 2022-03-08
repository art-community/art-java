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
        shutdownStorage();
        shutdown();
    }

    @AfterEach
    public void truncate() {
        current().truncate();
        other().truncate();
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
                .filter(filter -> filter
                        .byNumber(testingMetaModel().f1Field()).between(1, 3)
                        .and(nested -> nested
                                .byString(testingMetaModel().f16Field()).contains("test")
                                .or()
                                .byString(testingMetaModel().f16Field()).contains("test 2")))
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
                .filter(filter -> filter
                        .byField(testingMetaModel().f16Field())
                        .equal(data.get(1).getF16()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(filter -> filter
                        .byField(testingMetaModel().f16Field())
                        .notEqual(data.get(1).getF16()))
                .collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = current()
                .stream()
                .filter(filter -> filter
                        .byField(testingMetaModel().f1Field())
                        .in(data.get(0).getF1(), data.get(2).getF1()))
                .collect();
        assertEquals(2, result.size());
        data.get(0).assertEquals(result.get(0));
        data.get(2).assertEquals(result.get(1));

        result = current()
                .stream()
                .filter(filter -> filter
                        .byField(testingMetaModel().f1Field())
                        .notIn(data.get(0).getF1(), data.get(2).getF1()))
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
                .filter(filter -> filter
                        .byString(testingMetaModel().f16Field())
                        .contains(data.get(1).getF16()))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(filter -> filter
                        .byString(testingMetaModel().f16Field())
                        .startsWith("st"))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));

        result = current()
                .stream()
                .filter(filter -> filter
                        .byString(testingMetaModel().f16Field())
                        .endsWith("ng"))
                .collect();
        assertEquals(1, result.size());
        data.get(1).assertEquals(result.get(0));
    }

    @Test
    public void testStreamBySpaceWithNumbers() {
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
                .filter(filter -> filter
                        .bySpace(otherSpace(), testingMetaModel().f5Field())
                        .currentNumber(testingMetaModel().f1Field())
                        .lessThan(otherSpace().numberField())

                        .and()

                        .bySpace(otherSpace(), testingMetaModel().f5Field())
                        .otherString(otherSpace().valueField())
                        .contains("test")
                )
                .sort(testingMetaModel().f1Field(), Sorter::descendant)
                .collect();
        assertEquals(2, result.size());
        currentData.get(1).assertEquals(result.get(0));
        currentData.get(0).assertEquals(result.get(1));
    }

    @Test
    public void testStreamBySpaceWithStrings() {
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
                .filter(filter -> filter
                        .bySpace(otherSpace(), testingMetaModel().f5Field())
                        .currentString(testingMetaModel().f16Field())
                        .contains(otherSpace().valueField()))
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
                .filter(filter -> filter
                        .byString(testingMetaModel().f16Field())
                        .contains(data.get(1).getF16()))
                .count();
        assertEquals(1, result);

        boolean all = current()
                .stream()
                .filter(filter -> filter
                        .byNumber(testingMetaModel().f1Field())
                        .between(1, 2))
                .all(filter -> filter
                        .byField(testingMetaModel().f6Field())
                        .equal(true));
        assertTrue(all);

        boolean any = current()
                .stream()
                .any(filter -> filter
                        .byString(testingMetaModel().f16Field())
                        .startsWith("st"));
        assertTrue(any);
    }

    private static SpaceService<Integer, TestingMetaModel> current() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }

    private static SpaceService<Integer, OtherSpace> other() {
        return Tarantool.tarantool().space(OtherSpace.class);
    }
}
