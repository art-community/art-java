package io.art.rsocket;

import io.art.communicator.model.*;
import io.art.core.annotation.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;

@ForUsing
@UtilityClass
public class Rsocket {
    public static <T extends Connector> T rsocketConnector(Class<T> connectorClass) {
        return rsocketModule().configuration().getCommunicator().getConnectors().getConnector(connectorClass);
    }
}
