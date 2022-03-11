package io.art.tarantool.test;

import io.art.meta.test.*;
import io.art.meta.test.meta.*;
import io.art.storage.service.*;
import io.art.tarantool.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.test.meta.MetaMetaTest.MetaIoPackage.MetaArtPackage.MetaMetaPackage.MetaTestPackage.MetaTestingMetaModelClass.*;
import static io.art.tarantool.model.TarantoolIndexConfiguration.*;
import static io.art.tarantool.model.TarantoolSpaceConfiguration.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static io.art.tarantool.test.manager.TestTarantoolInstanceManager.*;
import static io.art.tarantool.test.meta.MetaTarantoolTest.MetaIoPackage.MetaArtPackage.MetaTarantoolPackage.MetaTestPackage.MetaModelPackage.MetaOtherSpaceClass.*;
import static io.art.transport.module.TransportActivator.*;

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

    }

    private static SpaceService<Integer, TestingMetaModel> current() {
        return Tarantool.tarantool().space(TestingMetaModel.class);
    }

    private static SpaceService<Integer, OtherSpace> other() {
        return Tarantool.tarantool().space(OtherSpace.class);
    }
}
