/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.entity.interceptor;

import ru.art.entity.Value;
import static ru.art.entity.interceptor.ValueInterceptionResult.*;
import java.util.function.Consumer;

public interface ValueInterceptor {
    ValueInterceptionResult intercept(Value value);

    static ValueInterceptor interceptAndContinue(Consumer<Value> consumer) {
        return value -> {
            consumer.accept(value);
            return nextInterceptor(value);
        };
    }

    static ValueInterceptor interceptAndCall(Consumer<Value> consumer) {
        return value -> {
            consumer.accept(value);
            return processHandling(value);
        };
    }

    static ValueInterceptor interceptAndReturn(Consumer<Value> consumer) {
        return value -> {
            consumer.accept(value);
            return stopHandling(value);
        };
    }
}
