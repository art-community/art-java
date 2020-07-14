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

package io.art.entity.interceptor;

import io.art.entity.*;
import static io.art.entity.interceptor.ValueInterceptionResult.*;
import java.util.function.*;

public interface ValueInterceptor<InValue extends Value, OutValue extends Value> {
    ValueInterceptionResult<InValue, OutValue> intercept(InValue value);

    static <InValue extends Value> ValueInterceptor<InValue, InValue> interceptAndContinue(Consumer<InValue> consumer) {
        return value -> {
            consumer.accept(value);
            return nextInterceptor(value);
        };
    }

    static <InValue extends Value> ValueInterceptor<InValue, InValue> interceptAndCall(Consumer<InValue> consumer) {
        return value -> {
            consumer.accept(value);
            return processHandling(value);
        };
    }

    static <InValue extends Value> ValueInterceptor<InValue, InValue> interceptAndReturn(Consumer<InValue> consumer) {
        return value -> {
            consumer.accept(value);
            return stopHandling(value);
        };
    }
}
