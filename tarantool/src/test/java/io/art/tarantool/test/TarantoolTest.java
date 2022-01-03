package io.art.tarantool.test;

import io.art.tarantool.communication.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.transport.module.TransportActivator.*;

public class TarantoolTest {
    @BeforeAll
    public static void setup() {
        initialize(logging(), meta(MetaTarantoolTest::new), transport(), tarantool());
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }

    @Test
    public void test() {
        UserStorage.UserSpace space = preconfiguredCommunicatorProxy(UserStorage.UserSpace.class, TarantoolCommunication::new).getCommunicator();
        space.saveUser(User.builder().id(1).name("test 1").build());
        space.saveUser(User.builder().id(2).name("test 2").build());
        space.saveUser(User.builder().id(3).name("test 3").build());
        System.out.println(space.getAllUsers());
    }
}
