package io.art.tarantool.test;

import io.art.meta.module.*;
import io.art.tarantool.communication.*;
import io.art.tarantool.test.TestStorage.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.transport.module.TransportActivator.*;

public class TarantoolTest {
    @BeforeAll
    public static void setup() {
        initialize(logging(), MetaActivator.meta(MetaTarantoolTest::new), transport(), tarantool());
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }

    @Test
    public void test() {
        TestSpace space = preconfiguredCommunicatorProxy(TestSpace.class, TarantoolCommunication::new).getCommunicator();
        TestRequest result = space.saveRequest(TestRequest.builder().data("test").build());
        System.out.println(result);
        System.out.println(space.getRequest(result.getId()));
        block();
    }
}
