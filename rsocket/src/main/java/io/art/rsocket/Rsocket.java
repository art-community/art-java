package io.art.rsocket;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.state.RsocketModuleState.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;

@Public
@UtilityClass
public class Rsocket {
    public static <T extends Portal> T rsocket(Class<T> portalClass) {
        return rsocketModule().configuration().getCommunicator().getPortals().getPortal(portalClass);
    }

    public static RsocketDefaultCommunicator rsocket() {
        return new RsocketDefaultWsCommunicator();
    }

    public RsocketLocalState rsocketState(MetaMethod<?> method) {
        return rsocketModule().state().rsocketState(method);
    }
}
