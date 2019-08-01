package ru.adk.rsocket.state;

import io.rsocket.RSocket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.adk.core.module.ModuleState;
import ru.adk.rsocket.server.RsocketServer;

public class RsocketModuleState implements ModuleState {
    private final ThreadLocal<CurrentRsocketState> currentClientSocket = new ThreadLocal<>();
    @Getter
    @Setter
    private RsocketServer server;

    public RsocketModuleState currentRocketState(CurrentRsocketState state) {
        currentClientSocket.set(state);
        return this;
    }

    public CurrentRsocketState currentRocketState() {
        return currentClientSocket.get();
    }

    @Getter
    @AllArgsConstructor
    public static class CurrentRsocketState {
        private final String dataMimeType;
        private final String metadataMimeType;
        private final RSocket RSocket;
    }
}
