package io.art.communicator.test;

import io.art.communicator.action.*;
import io.art.communicator.test.meta.*;
import io.art.communicator.test.meta.MetaCommunicatorTest.MetaIoPackage.MetaArtPackage.MetaCommunicatorPackage.MetaTestPackage.MetaProxyPackage.*;
import io.art.communicator.test.proxy.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import org.junit.jupiter.api.*;
import static io.art.communicator.proxy.CommunicatorProxyFactory.*;
import static io.art.communicator.test.factory.TestCommunicatorActionFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.meta.module.MetaModule.*;

public class CommunicatorTest {
    @BeforeAll
    public static void setup() {
        initialize(meta(MetaCommunicatorTest::new));
    }

    @Test
    public void testCommunicatorActionExecution() {
        MetaTestCommunicatorClass communicatorClass = cast(declaration(TestCommunicator.class));
        ImmutableMap<MetaMethod<?>, CommunicatorAction> actions = ImmutableMap.<MetaMethod<?>, CommunicatorAction>immutableMapBuilder()
                .put(communicatorClass.m1Method(), communicatorAction(communicatorClass, communicatorClass.m1Method()))
                .put(communicatorClass.m2Method(), communicatorAction(communicatorClass, communicatorClass.m2Method()))
                .build();
        TestCommunicator communicator = communicatorProxy(communicatorClass, actions);
        communicator.m1();
        communicator.m2();
    }
}
