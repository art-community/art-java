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

package io.art.logging;

import io.art.core.model.*;
import io.art.entity.immutable.*;
import io.art.entity.interceptor.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.model.InterceptionResult.*;
import static io.art.logging.LoggingModule.*;
import static io.art.logging.LoggingModuleConstants.LoggingParameters.*;
import static io.art.logging.LoggingModuleConstants.*;
import static io.art.logging.ThreadContextExtensions.*;
import static java.text.MessageFormat.*;
import static org.apache.logging.log4j.ThreadContext.*;

public class LoggingValueInterceptor implements ValueInterceptor {
    @Override
    public InterceptionResult intercept(Value value) {
        putIfNotNull(REQUEST_VALUE_KEY, value);
        logger(LoggingValueInterceptor.class).info(format(VALUE_LOG_MESSAGE, value));
        remove(REQUEST_VALUE_KEY);
        return cast(next(value));
    }
}
