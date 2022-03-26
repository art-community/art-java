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

package io.art.core.module;

import io.art.core.context.*;
import io.art.core.property.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.property.DisposableProperty.*;

public class StatefulModuleProxy<Configuration extends ModuleConfiguration, State extends ModuleState> {
    private final DisposableProperty<StatefulModule<Configuration, ?, State>> module;

    public StatefulModuleProxy(DisposableProperty<ManagedModule> managed) {
        this.module = disposable(() -> cast(managed.get().getModule()));
        module.disposed(ignore -> managed.dispose());
        managed.initialized(initialized -> initialized.onUnload(ignore -> module.dispose()));
    }

    public Configuration configuration() {
        return module.get().getConfiguration();
    }

    public State state() {
        return module.get().getState();
    }
}
