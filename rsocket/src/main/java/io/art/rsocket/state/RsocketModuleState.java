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

import io.art.core.module.*;
import io.rsocket.*;
import lombok.*;
import reactor.util.context.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ContextKeys.*;
import java.util.*;

public class RsocketModuleState implements ModuleState {
    @Getter
    private final List<RSocket> connectedClients = linkedListOf();

    private final ThreadLocal<RsocketThreadLocalState> threadLocalState = new ThreadLocal<>();

    public RSocket registerClient(RSocket rsocket) {
        connectedClients.add(rsocket);
        return rsocket;
    }

    public RsocketModuleState setThreadLocalState(RsocketThreadLocalState state) {
        threadLocalState.set(state);
        return this;
    }

    @Getter
    @RequiredArgsConstructor
    public static class RsocketThreadLocalState {
        private RSocket requesterRsocket;

        public static RsocketThreadLocalState fromContext(Context context) {
            RsocketThreadLocalState localState = new RsocketThreadLocalState();
            localState.requesterRsocket = context.get(REQUESTER_RSOCKET_KEY);
            return localState;
        }
    }
}
