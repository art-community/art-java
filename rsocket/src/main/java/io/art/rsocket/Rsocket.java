package io.art.rsocket;

import io.art.communicator.proxy.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.rsocket.module.RsocketModule.*;

@UtilityClass
public class Rsocket {
    public static <T extends Connector> T rsocketConnector(Class<T> proxyClass) {
        return cast(rsocketModule().configuration().getConnectorProvider().get().get(proxyClass));
    }
}
