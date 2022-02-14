package io.art.tarantool.test;

import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.*;
import io.art.tarantool.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.TestingMetaModelGenerator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolIndexPartConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.storage.TestTarantoolStorage.*;
import static io.art.transport.module.TransportActivator.*;

public class TarantoolStorageTest {
    @BeforeAll
    public static void setup() {
        initializeStorage();
        initialize(logging(),
                meta(() -> new MetaTarantoolTest(new MetaMetaTest())),
                transport(),
                tarantool(tarantool -> tarantool
                        .storage(TestStorage.class, storage -> storage.client(client -> client
                                .port(STORAGE_PORT)
                                .username(USERNAME)
                                .logging(true)
                                .password(PASSWORD)))
                        .space(TestStorage.class, int.class, TestingMetaModel.class)
                )
        );
        Tarantool.tarantool()
                .schema(TestStorage.class)
                .createSpace(spaceFor(TestingMetaModel.class).ifNotExists(true).build())
                .createIndex(indexFor(TestingMetaModel.class, testingMetaModel().f1Field())
                        .ifNotExists(true)
                        .unique(true)
                        .part(indexPartFor(testingMetaModel().f1Field()))
                        .build());
    }

    @AfterAll
    public static void cleanup() {
        shutdownStorage();
        shutdown();
    }

    @Test
    public void testSinglePut() {
        TestingMetaModel data = generateTestingModel();
        data.assertEquals(space().put(data));
    }

    @Test
    public void testFindFirst() {
        TestingMetaModel data = generateTestingModel();
        space().put(data);
        data.assertEquals(space().findFirst(1));
    }

    private static SpaceService<Integer, TestingMetaModel> space() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }
}
