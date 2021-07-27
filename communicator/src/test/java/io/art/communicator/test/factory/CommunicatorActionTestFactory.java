package io.art.communicator.test.factory;

import io.art.communicator.*;
import io.art.communicator.action.*;
import io.art.communicator.action.CommunicatorAction.*;
import io.art.communicator.configuration.*;
import io.art.communicator.decorator.*;
import io.art.communicator.refresher.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.MethodDecoratorScope.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.model.CommunicatorActionIdentifier.*;
import static java.util.Objects.*;

@UtilityClass
public class CommunicatorActionTestFactory {
    public CommunicatorAction communicatorAction(MetaClass<?> owner, MetaMethod<?> method, Communication implementation) {
        return communicatorAction(communicatorActionId(owner.definition().type().getSimpleName(), method.name()), method, implementation);
    }

    public CommunicatorAction communicatorAction(CommunicatorActionIdentifier id, MetaMethod<?> method, Communication communication) {
        MetaType<?> inputType = orNull(() -> immutableArrayOf(method.parameters().values()).get(0).type(), isNotEmpty(method.parameters()));
        CommunicatorConfiguration configuration = CommunicatorConfiguration.builder().refresher(new CommunicatorRefresher()).build();
        CommunicatorActionBuilder builder = CommunicatorAction.builder()
                .id(id)
                .outputType(method.returnType())
                .communication(communication)
                .inputDecorator(new CommunicatorDeactivationDecorator(id, configuration))
                .inputDecorator(new CommunicatorLoggingDecorator(id, configuration, INPUT))
                .outputDecorator(new CommunicatorLoggingDecorator(id, configuration, OUTPUT));
        if (nonNull(inputType)) {
            CommunicatorAction action = builder.inputType(inputType).build();
            action.initialize();
            return action;
        }
        CommunicatorAction action = builder.build();
        action.initialize();
        return action;
    }
}
