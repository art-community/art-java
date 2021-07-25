package io.art.communicator.test;

import io.art.communicator.action.*;
import io.art.communicator.test.meta.*;
import io.art.communicator.test.meta.MetaCommunicatorTest.MetaIoPackage.MetaArtPackage.MetaCommunicatorPackage.MetaTestPackage.MetaProxyPackage.*;
import io.art.communicator.test.proxy.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
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
        CommunicatorAction action = communicatorAction(communicatorClass, communicatorClass.m1Method());
        action.communicate(Flux.just("test"));
    }
}
