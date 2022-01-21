package io.art.rsocket.communicator;

import io.art.core.annotation.*;

@Public
public interface RsocketDefaultCommunicator {
    default RsocketDefaultWsCommunicator ws() {
        return new RsocketDefaultWsCommunicator();
    }

    default RsocketDefaultTcpCommunicator tcp() {
        return new RsocketDefaultTcpCommunicator();
    }

    void dispose();
}
