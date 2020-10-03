/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.rsocket.state;

import io.art.entity.constants.*;
import io.art.entity.constants.EntityConstants.*;
import io.rsocket.*;
import lombok.*;
import io.art.core.module.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.server.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

public class RsocketModuleState implements ModuleState {
    private final ThreadLocal<CurrentRsocketState> currentClientSocket = new ThreadLocal<>();
    @Getter
    private final List<RSocket> rsocketClients = linkedListOf();

    @Getter
    @Setter
    private RsocketServer tcpServer;

    @Getter
    @Setter
    private RsocketServer webSocketServer;

    public RsocketModuleState currentRocketState(CurrentRsocketState state) {
        currentClientSocket.set(state);
        return this;
    }

    public CurrentRsocketState currentRocketState() {
        return currentClientSocket.get();
    }

    public RSocket registerRsocket(RSocket rsocket) {
        rsocketClients.add(rsocket);
        return rsocket;
    }

    @Getter
    @Builder
    public static class CurrentRsocketState {
        private final String dataMimeType;
        private final String metadataMimeType;
        private final DataFormat dataFormat;
        private final RSocket rsocket;
    }
}
