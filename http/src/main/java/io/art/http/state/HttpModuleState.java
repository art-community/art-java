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

package io.art.http.state;

import io.art.core.module.*;
import io.art.server.module.*;

import java.util.function.*;

public class HttpModuleState implements ModuleState {
    private final ThreadLocal<HttpContext> threadLocalContext = new ThreadLocal<>();

    public void localContext(Function<HttpContext, HttpContext> functor) {
        threadLocalContext.set(functor.apply(threadLocalContext.get()));
    }

    public void localContext(HttpContext context) {
        threadLocalContext.set(context);
    }

    public HttpContext localContext() {
        return ServerModule.serverModule().state().localState().getContext().get(HttpContext.class);
    }



}
