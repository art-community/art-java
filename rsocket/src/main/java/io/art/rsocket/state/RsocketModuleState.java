/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.state;

import io.art.core.module.*;
import io.art.meta.model.*;
import io.art.rsocket.model.*;
import io.rsocket.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import java.util.*;

public class RsocketModuleState implements ModuleState {
    private final MetaLocalState<RsocketLocalState> localState = new MetaLocalState<>();

    public void rsocketState(MetaMethod<? extends MetaClass<?>, ?> method, RsocketLocalState state) {
        localState.set(cast(method), state);
    }

    public RsocketLocalState rsocketState(MetaMethod<? extends MetaClass<?>, ?> method) {
        return localState.get(cast(method));
    }

    public void clearRsocketState(MetaMethod<? extends MetaClass<?>, ?> method) {
        localState.remove(cast(method));
    }

    @Getter
    @Builder
    @SuppressWarnings(OPTIONAL_USED_AS_FIELD)
    public static class RsocketLocalState {
        @Builder.Default
        private final Optional<RsocketSetupPayload> setupPayloadModel = Optional.empty();
        private final ConnectionSetupPayload setupPayloadRaw;
    }
}
