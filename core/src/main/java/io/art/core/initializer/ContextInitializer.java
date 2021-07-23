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

package io.art.core.initializer;

import io.art.core.configuration.*;
import io.art.core.module.*;
import io.art.core.singleton.*;
import lombok.experimental.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.core.singleton.SingletonAction.*;
import static java.util.Arrays.*;

@UtilityClass
public class ContextInitializer {
    private final static SingletonAction initialize = singletonAction();

    public static void initialize(ModuleFactory<?>... modules) {
        initialize.run(() -> initializeModules(modules));
    }

    private static void initializeModules(ModuleFactory<?>... modules) {
        prepareInitialization(ContextConfiguration.builder().build(), emptyConsumer());
        processInitialization(stream(modules).map(ModuleFactory::get).collect(immutableSetCollector()));
    }
}
