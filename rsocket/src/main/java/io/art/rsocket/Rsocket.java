package io.art.rsocket;

import io.art.communicator.model.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;

@UtilityClass
public class Rsocket {
    public static <T extends Connector> T rsocketConnector(Class<T> connectorClass) {
        return rsocketModule().configuration().getCommunicator().getConnectors().getConnector(connectorClass);
    }
}
