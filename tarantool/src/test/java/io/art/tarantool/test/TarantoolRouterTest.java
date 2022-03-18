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
import static io.art.core.factory.ArrayFactory.*;
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
import java.util.*;
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
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(current().sharded().put(data));
    }

    @Test
    public void testImmutable() {
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
