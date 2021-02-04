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

import io.art.core.collection.*;
import io.art.core.managed.*;
import io.art.core.module.*;
import io.art.rsocket.model.*;
import io.rsocket.*;
import lombok.*;
import reactor.util.context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.managed.ChangesListener.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ContextKeys.*;
import java.util.*;
import java.util.function.*;

public class RsocketModuleState implements ModuleState {
    private final List<RSocket> requesters = linkedListOf();
    private final ThreadLocal<RsocketThreadLocalState> threadLocalState = new ThreadLocal<>();

    public void registerRequester(RSocket socket) {
        requesters.add(socket);
    }

    public void removeRequester(RSocket socket) {
        requesters.remove(socket);
    }

    public ImmutableArray<RSocket> getRequesters() {
        return immutableArrayOf(requesters);
    }


    public void localState(Function<RsocketThreadLocalState, RsocketThreadLocalState> functor) {
        threadLocalState.set(functor.apply(threadLocalState.get()));
    }

    public void localState(RsocketThreadLocalState state) {
        threadLocalState.set(state);
    }

    public RsocketThreadLocalState localState() {
        return threadLocalState.get();
    }


    @Getter
    @Builder(toBuilder = true)
    public static class RsocketThreadLocalState {
        private final RSocket requesterRsocket;
        private final RsocketSetupPayload setupPayload;

        public static RsocketThreadLocalState fromContext(Context context) {
            RSocket requesterRsocket = context.get(REQUESTER_RSOCKET_KEY);
            RsocketSetupPayload setupPayload = context.get(SETUP_PAYLOAD_KEY);
            return new RsocketThreadLocalState(requesterRsocket, setupPayload);
        }
    }
}
