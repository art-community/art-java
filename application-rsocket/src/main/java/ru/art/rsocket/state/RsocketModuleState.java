/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.rsocket.state;

import io.rsocket.*;
import lombok.*;
import ru.art.core.module.*;
import ru.art.rsocket.constants.RsocketModuleConstants.*;
import ru.art.rsocket.exception.*;
import ru.art.rsocket.server.*;
import static java.text.MessageFormat.*;
import static java.util.concurrent.ConcurrentHashMap.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.CONNECTED_RSOCKET_NOT_FOUND;
import java.text.*;
import java.util.*;

public class RsocketModuleState implements ModuleState {
    private final Set<ConnectedRsocketState> connectedRsockets = newKeySet();
    @Getter
    private final List<RSocket> rsocketClients = linkedListOf();

    @Getter
    @Setter
    private RsocketServer tcpServer;

    @Getter
    @Setter
    private RsocketServer webSocketServer;

    public RsocketModuleState connectRsocket(ConnectedRsocketState state) {
        connectedRsockets.add(state);
        return this;
    }

    public ConnectedRsocketState connectedRsocket(RSocket rsocket) {
        return connectedRsockets
                .stream()
                .filter(state -> state.rsocket.equals(rsocket))
                .findFirst()
                .orElseThrow(() -> new RsocketServerException(format(CONNECTED_RSOCKET_NOT_FOUND, rsocket)));
    }


    public Set<ConnectedRsocketState> connectedRsockets() {
        return connectedRsockets;
    }

    public RSocket registerRsocket(RSocket rsocket) {
        rsocketClients.add(rsocket);
        return rsocket;
    }

    @Getter
    @Builder
    @EqualsAndHashCode
    public static class ConnectedRsocketState {
        private final String dataMimeType;
        private final String metadataMimeType;
        private final RsocketDataFormat dataFormat;
        private final RSocket rsocket;
    }
}
