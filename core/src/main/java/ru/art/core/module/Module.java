/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.module;

import ru.art.core.exception.*;
import ru.art.core.identified.*;
import static java.text.MessageFormat.*;
import static ru.art.core.constants.ExceptionMessages.*;

public interface Module<C extends ModuleConfiguration, S extends ModuleState> extends UniqueIdentified {
    default void onLoad() {
    }

    default void onUnload() {
    }

    default void reload() {
    }

    C getDefaultConfiguration();

    default S getState() {
        throw new InternalRuntimeException(format(MODULE_HAS_NOT_STATE, getId()));
    }
}
