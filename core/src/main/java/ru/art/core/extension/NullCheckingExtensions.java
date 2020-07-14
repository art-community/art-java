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

package ru.art.core.extension;

import lombok.experimental.*;
import static java.util.Objects.*;
import java.util.function.*;

@UtilityClass
public class NullCheckingExtensions {
    public static <T> T getOrElse(T object, T orElse) {
        return isNull(object) ? orElse : object;
    }

    public static <T> T nullOrElse(Object val, T orElse) {
        return isNull(val) ? null : orElse;
    }

    public static <T, R> R doIfNotNull(T val, Function<T, R> action) {
        return nonNull(val) ? action.apply(val) : null;
    }

    public static <T, R> R doIfNotNull(T val, Function<T, R> action, Supplier<R> orElse) {
        return nonNull(val) ? action.apply(val) : orElse.get();
    }

    public static <T> void doIfNotNull(T val, Consumer<T> consumer) {
        if (nonNull(val)) {
            consumer.accept(val);
        }
    }
}
