/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.server.state;

import io.art.core.module.*;
import io.art.server.method.*;
import lombok.*;
import reactor.util.context.*;
import static io.art.server.constants.ServerModuleConstants.StateKeys.*;
import java.util.function.*;

@Getter
public class ServerModuleState implements ModuleState {
    private final ThreadLocal<ServerThreadLocalState> threadLocalState = new ThreadLocal<>();

    public void localState(Function<ServerThreadLocalState, ServerThreadLocalState> functor) {
        threadLocalState.set(functor.apply(threadLocalState.get()));
    }

    public void localState(ServerThreadLocalState state) {
        threadLocalState.set(state);
    }

    public ServerThreadLocalState localState() {
        return threadLocalState.get();
    }

    @Getter
    @Builder(toBuilder = true)
    public static class ServerThreadLocalState {
        private final ContextView context;
        private final ServiceMethod specification;

        public static ServerThreadLocalState fromContext(ContextView context) {
            ServiceMethod specification = context.get(SERVICE_METHOD_ID);
            return new ServerThreadLocalState(context, specification);
        }
    }
}
