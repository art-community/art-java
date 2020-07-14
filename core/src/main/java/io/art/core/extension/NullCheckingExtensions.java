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

package io.art.core.extension;

import lombok.experimental.*;
import static java.util.Objects.*;
import java.util.function.*;

@UtilityClass
public class NullCheckingExtensions {
    public static <T> T getOrElse(T value, T orElse) {
        return isNull(value) ? orElse : value;
    }

    public static <T> T getOrElse(T value, Supplier<T> orElse) {
        return isNull(value) ? orElse.get() : value;
    }

    public static <T> T replaceNull(Object value, T orElse) {
        return isNull(value) ? null : orElse;
    }

    public static <T> void apply(T value, Consumer<T> consumer) {
        if (nonNull(value)) {
            consumer.accept(value);
        }
    }

    public static <T, R> R let(T value, Function<T, R> action) {
        return nonNull(value) ? action.apply(value) : null;
    }

    public static <T, R> R let(T value, Function<T, R> action, Supplier<R> orElse) {
        return nonNull(value) ? action.apply(value) : orElse.get();
    }
}
