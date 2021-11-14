package io.art.rsocket;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.rsocket.state.RsocketModuleState.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.function.*;

@Public
@UtilityClass
public class Rsocket {
    public static <T extends Connector> T rsocketConnector(Class<T> connectorClass) {
        return rsocketModule().configuration().getCommunicator().getConnectors().getConnector(connectorClass);
    }

    public <C, M extends MetaClass<C>> RsocketLocalState rsocketState(Class<C> owner, Function<M, MetaMethod<?>> method) {
        return rsocketModule().state().rsocketState(owner, method);
    }
}
