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

package io.art.core.handler;

import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import java.util.function.*;

@RequiredArgsConstructor
public class CauseHandler {
    private final Throwable outer;
    private Object result;

    public static CauseHandler handleCause(Throwable outer) {
        return new CauseHandler(outer);
    }

    public <C> CauseHandler handle(Class<C> causeClass, Function<C, Object> handler) {
        Throwable cause = outer.getCause();
        if (isNotEmpty(cause) && cause.getClass().equals(causeClass)) {
            result = handler.apply(cast(cause));
        }
        return this;
    }

    public <C> CauseHandler consume(Class<C> causeClass, Consumer<C> handler) {
        Throwable cause = outer.getCause();
        if (isNotEmpty(cause) && cause.getClass().equals(causeClass)) {
            handler.accept(cast(cause));
        }
        return this;
    }

    public <T> T getResult() {
        return cast(result);
    }

    public <T> T getResult(T defaultResult) {
        return isEmpty(result) ? defaultResult : cast(result);
    }
}
