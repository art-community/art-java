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

package io.art.server.service.exception;

import io.art.server.model.*;
import lombok.*;
import static com.google.common.base.Throwables.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static java.text.MessageFormat.*;

@Getter
public class ServiceExecutionException extends RuntimeException {
    private final String errorMessage;
    private final String stackTraceText;

    public ServiceExecutionException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.stackTraceText = getStackTraceAsString(this);
    }

    public ServiceExecutionException(String errorMessage, String stackTraceText) {
        super(format(SERVICE_EXECUTION_EXCEPTION_WITH_STACK_TRACE, errorMessage, stackTraceText));
        this.errorMessage = errorMessage;
        this.stackTraceText = stackTraceText;
    }

    public ServiceExecutionException(Throwable throwable) {
        super(formatFullErrorMessage(throwable), throwable);
        this.errorMessage = throwable.getMessage();
        this.stackTraceText = getStackTraceAsString(throwable);
    }

    public ServiceExecutionException(ServiceMethodCommand command, String errorMessage) {
        super(format(SERVICE_EXECUTION_EXCEPTION_MESSAGE, command.getServiceId(), command.getMethodId(), errorMessage));
        this.errorMessage = errorMessage;
        this.stackTraceText = getStackTraceAsString(this);
    }

    public ServiceExecutionException(ServiceMethodCommand command, String errorMessage, String stackTraceText) {
        super(format(SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE, command.getServiceId(), command.getMethodId(), errorMessage, stackTraceText));
        this.errorMessage = errorMessage;
        this.stackTraceText = stackTraceText;
    }

    public ServiceExecutionException(ServiceMethodCommand command, Throwable throwable) {
        super(formatFullErrorMessage(command, throwable), throwable);
        this.errorMessage = throwable.getMessage();
        this.stackTraceText = getStackTraceAsString(throwable);
    }

    private static String formatFullErrorMessage(ServiceMethodCommand command, Throwable throwable) {
        return format(SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE,
                command.getServiceId(),
                command.getMethodId(),
                throwable.getMessage(),
                getStackTraceAsString(throwable)
        );
    }

    private static String formatFullErrorMessage(Throwable throwable) {
        return format(SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE_WITHOUT_COMMAND,
                throwable.getMessage(),
                getStackTraceAsString(throwable)
        );
    }
}
