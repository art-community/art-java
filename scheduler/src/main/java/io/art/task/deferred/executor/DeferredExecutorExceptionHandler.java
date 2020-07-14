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

package io.art.task.deferred.executor;

import static java.text.MessageFormat.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.logging.LoggingModule.*;
import static io.art.task.deferred.executor.SchedulerModuleExceptions.*;

/**
 * Дефолтный обработчик ошибок выполнения отложенных событей
 */
public class DeferredExecutorExceptionHandler implements ExceptionHandler {
    @Override
    public void onException(ExceptionEvent event, Throwable throwable) {
        loggingModule()
                .getLogger(DeferredExecutorExceptionHandler.class)
                .error(format(EXCEPTION_OCCURRED_DURING, event.getMessage(), ifEmpty(throwable.getMessage(), throwable.getClass()), throwable));
    }
}
