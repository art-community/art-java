/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.http.state;

import io.art.core.module.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import java.util.function.*;

public class HttpModuleState implements ModuleState {
    private final MetaLocalState<LazyProperty<HttpLocalState>> httpLocalState = new MetaLocalState<>();
    private final MetaLocalState<WsLocalState> wsLocalState = new MetaLocalState<>();

    public void httpState(MetaClass<?> owner, MetaMethod<?> method, LazyProperty<HttpLocalState> state) {
        httpLocalState.set(owner, method, state);
    }

    public <C, M extends MetaClass<C>> HttpLocalState httpState(Class<C> owner, Function<M, MetaMethod<?>> method) {
        return httpLocalState.get(owner, method).get();
    }

    public <C, M extends MetaClass<C>> void clearHttpState(MetaClass<?> owner, MetaMethod<?> method) {
        httpLocalState.remove(owner, method);
    }

    public void wsState(MetaClass<?> owner, MetaMethod<?> method, WsLocalState state) {
        wsLocalState.set(owner, method, state);
    }

    public <C, M extends MetaClass<C>> WsLocalState wsState(Class<C> owner, Function<M, MetaMethod<?>> method) {
        return wsLocalState.get(owner, method);
    }

    public <C, M extends MetaClass<C>> void clearWsState(MetaClass<?> owner, MetaMethod<?> method) {
        wsLocalState.remove(owner, method);
    }
}
