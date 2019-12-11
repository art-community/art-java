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

package ru.art.service.exception;

import lombok.*;
import ru.art.service.model.*;
import static com.google.common.base.Throwables.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ServiceExceptionsMessages.*;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ServiceExecutionException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public ServiceExecutionException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceExecutionException(String errorCode, Throwable throwable) {
        super(formatErrorMessage(errorCode, throwable), throwable);
        this.errorCode = errorCode;
        this.errorMessage = throwable.getMessage();
    }

    public ServiceExecutionException(ServiceMethodCommand command, String errorCode, String errorMessage) {
        super(format(SERVICE_EXECUTION_EXCEPTION_MESSAGE, command.getServiceId(), command.getMethodId(), errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceExecutionException(ServiceMethodCommand command, String errorCode, Throwable throwable) {
        super(formatErrorMessage(command, errorCode, throwable), throwable);
        this.errorCode = errorCode;
        this.errorMessage = throwable.getMessage();
    }

    private static String formatErrorMessage(ServiceMethodCommand command, String errorCode, Throwable throwable) {
        return format(SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE, command.getServiceId(), command.getMethodId(),
                errorCode, throwable.getMessage(), getStackTraceAsString(throwable));
    }

    private static String formatErrorMessage(String errorCode, Throwable throwable) {
        return format(SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE_WITHOUT_COMMAND, errorCode, throwable.getMessage(), getStackTraceAsString(throwable));
    }
}
