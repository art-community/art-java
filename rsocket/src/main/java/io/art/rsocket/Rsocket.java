package io.art.rsocket;

import io.art.communicator.model.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.rsocket.module.RsocketModule.*;

@UtilityClass
public class Rsocket {
    public static <T extends Connector> T rsocketConnector(Class<T> connectorClass) {
        return cast(rsocketModule().configuration().getConnectorProvider().get().get(connectorClass));
    }

    public static <T extends Communicator> T rsocketCommunicator(Class<T> communicatorClass) {
        return cast(rsocketModule().configuration().getCommunicatorProvider().get().get(communicatorClass));
    }
}
