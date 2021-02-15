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

package io.art.http.state;

import io.art.core.module.*;
import lombok.*;
import reactor.util.context.*;
import java.util.function.*;

public class HttpModuleState implements ModuleState {
    private final ThreadLocal<HttpThreadLocalState> threadLocalState = new ThreadLocal<>();

    public void localState(Function<HttpThreadLocalState, HttpThreadLocalState> functor) {
        threadLocalState.set(functor.apply(threadLocalState.get()));
    }

    public void localState(HttpThreadLocalState state) {
        threadLocalState.set(state);
    }

    public HttpThreadLocalState localState() {
        return threadLocalState.get();
    }


    @Getter
    @Builder(toBuilder = true)
    public static class HttpThreadLocalState {

        public static HttpThreadLocalState fromContext(Context context) {
            return new HttpThreadLocalState();
        }
    }
}
