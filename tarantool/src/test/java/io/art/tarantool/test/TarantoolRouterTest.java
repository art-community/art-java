package io.art.tarantool.test;

import io.art.core.collection.*;
import io.art.meta.module.*;
import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.service.*;
import io.art.tarantool.module.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import io.art.transport.module.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.Tarantool.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.manager.TestTarantoolInstanceManager.*;
import static io.art.tarantool.test.model.TestStorage.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.*;

public class TarantoolRouterTest {
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
                        .space(TestStorage.class, TestingMetaModel.class, space -> space
                                .indexes(TestModelIndexes.class)
                                .sharders(TestModelSharders.class))
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

    @AfterAll
    public static void cleanup() {
        shutdownStorage();
        shutdown();
    }

    @Test
    public void testMutable() {
        TestingMetaModel data1 = generateTestingModel().toBuilder().f16("bucket-1").f1(1).build();
        TestingMetaModel data2 = generateTestingModel().toBuilder().f16("bucket-2").f1(2).build();

        data1.assertEquals(current().sharded(testModelSharders().string(), "bucket-1").put(data1));
        data2.assertEquals(current().sharded(testModelSharders().string(), "bucket-2").put(data2));

        ImmutableArray<TestingMetaModel> result1 = current().sharded(testModelSharders().string(), "bucket-1").select(1);
        ImmutableArray<TestingMetaModel> result2 = current().sharded(testModelSharders().string(), "bucket-2").select(2);
        ImmutableArray<TestingMetaModel> emptyResult = current().sharded(testModelSharders().string(), "bucket-2").select(1);

        assertEquals(0, emptyResult.size());

        assertEquals(1, result1.size());
        data1.assertEquals(result1.get(0));

        assertEquals(2, result2.size());
        data2.assertEquals(result2.get(0));
    }


    @Test
    public void testSubscription() {
        tarantool(TestStorage.class).testSubscription();
        assertTrue(TestService.await());
    }

    @Test
    public void testChannel() {
        CountDownLatch waiter = new CountDownLatch(2);
        tarantool(TestStorage.class).channel().testChannel().subscribe(value -> {
            assertEquals("test", value);
            waiter.countDown();
        });
        wrapExceptionCall(() -> waiter.await(30, TimeUnit.SECONDS), Assertions::fail);
    }

    private static BlockingSpaceService<Integer, TestingMetaModel> current() {
        return tarantool().space(TestingMetaModel.class);
    }
}
