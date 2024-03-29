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

package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.core.module.*;
import io.art.meta.module.*;
import io.art.rsocket.meta.*;
import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.module.ModuleActivator.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Public
@UtilityClass
public class RsocketActivator {
    public static ModuleActivator rsocket() {
        return rsocket(identity());
    }

    public static ModuleActivator rsocket(UnaryOperator<RsocketInitializer> initializer) {
        return module(RsocketModule.class, RsocketModule::new, () -> initializer.apply(new RsocketInitializer()))
                .<MetaInitializer>decorator(META_MODULE_ID, meta -> meta.registerLibrary(MetaRsocket::new));
    }
}
