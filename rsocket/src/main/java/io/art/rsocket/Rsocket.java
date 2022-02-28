package io.art.rsocket;

import io.art.communicator.*;
import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.state.RsocketModuleState.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;
import static lombok.AccessLevel.*;

@Public
@UtilityClass
public class Rsocket {
    private final static RsocketProvider provider = new RsocketProvider();

    public static <T extends Communicator> T rsocket(Class<T> communicatorClass) {
        return rsocketModule().configuration().getCommunicator().getCommunicators().getCommunicator(communicatorClass).getCommunicator();
    }

    public static RsocketProvider rsocket() {
        return provider;
    }

    public RsocketLocalState rsocketState(MetaMethod<? extends MetaClass<?>, ?> method) {
        return rsocketModule().state().rsocketState(method);
    }

    @Public
    @NoArgsConstructor(access = PRIVATE)
    public static class RsocketProvider {
        public RsocketDefaultTcpCommunicator tcp() {
            return new RsocketDefaultTcpCommunicator();
        }

        public RsocketDefaultWsCommunicator ws() {
            return new RsocketDefaultWsCommunicator();
        }
    }
}
