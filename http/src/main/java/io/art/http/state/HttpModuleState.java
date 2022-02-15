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

public class HttpModuleState implements ModuleState {
    private final MetaLocalState<LazyProperty<HttpLocalState>> httpLocalState = new MetaLocalState<>();
    private final MetaLocalState<WsLocalState> wsLocalState = new MetaLocalState<>();

    public void httpState(MetaMethod<MetaClass<?>, ?> method, LazyProperty<HttpLocalState> state) {
        httpLocalState.set(method, state);
    }

    public HttpLocalState httpState(MetaMethod<MetaClass<?>, ?> method) {
        return httpLocalState.get(method).get();
    }

    public void clearHttpState(MetaMethod<MetaClass<?>, ?> method) {
        httpLocalState.remove(method);
    }

    public void wsState(MetaMethod<MetaClass<?>, ?> method, WsLocalState state) {
        wsLocalState.set(method, state);
    }

    public WsLocalState wsState(MetaMethod<MetaClass<?>, ?> method) {
        return wsLocalState.get(method);
    }

    public void clearWsState(MetaMethod<MetaClass<?>, ?> method) {
        wsLocalState.remove(method);
    }
}
