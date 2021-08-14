package io.art.rsocket;

import io.art.core.annotation.*;
import io.art.core.communication.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;

@ForUsing
@UtilityClass
public class Rsocket {
    public static <T extends Connector> T rsocketConnector(Class<T> connectorClass) {
        return rsocketModule().configuration().getCommunicator().getConnectors().getConnector(connectorClass);
    }
}
