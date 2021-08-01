package io.art.rsocket;

import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.rsocket.module.RsocketModule.*;

@UtilityClass
public class Rsocket {
    public static <T> T rsocketCommunicator(Class<T> proxyClass) {
        return cast(rsocketModule().configuration().getCommunicatorProxyProvider().get().get(proxyClass).getProxy());
    }
}
