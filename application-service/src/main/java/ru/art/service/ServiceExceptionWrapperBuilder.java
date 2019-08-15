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

package ru.art.service;

import static java.util.Objects.isNull;
import static ru.art.service.constants.ServiceExceptionsMessages.THROWABLE_EXCEPTION_WRAPPER_IS_NULL;
import static ru.art.service.constants.ServiceExceptionsMessages.WRAPPER_IS_NULL;

public class ServiceExceptionWrapperBuilder {
    private ServiceExecutionExceptionWrapper exceptionWrapper;

    public static ServiceExceptionWrapperBuilder exceptionWrapperBuilder() {
        return new ServiceExceptionWrapperBuilder();
    }

    public ServiceExceptionWrapperBuilder addExceptionWrapper(ServiceExecutionExceptionWrapper wrapper) {
        if (isNull(wrapper)) {
            throw new NullPointerException(WRAPPER_IS_NULL);
        }
        if (isNull(exceptionWrapper)) {
            exceptionWrapper = wrapper;
            return this;
        }
        ServiceExecutionExceptionWrapper currentWrapper = exceptionWrapper;
        ServiceExecutionExceptionWrapper lastWrapper = exceptionWrapper.getPreviousWrapper();
        while (lastWrapper != null) {
            currentWrapper = lastWrapper;
            lastWrapper = currentWrapper.getPreviousWrapper();
        }
        currentWrapper.setPreviousWrapper(wrapper);
        return this;
    }

    public ServiceExceptionWrapperBuilder setThrowableExceptionWrapper(ServiceExecutionExceptionWrapper throwableExceptionWrapper) {
        if (isNull(throwableExceptionWrapper)) {
            throw new NullPointerException(THROWABLE_EXCEPTION_WRAPPER_IS_NULL);
        }
        if (isNull(exceptionWrapper)) {
            exceptionWrapper = throwableExceptionWrapper;
            return this;
        }
        throwableExceptionWrapper.setPreviousWrapper(exceptionWrapper);
        exceptionWrapper = throwableExceptionWrapper;
        return this;
    }

    public ServiceExecutionExceptionWrapper build() {
        addExceptionWrapper(new ServiceExecutionExceptionWrapper());
        return exceptionWrapper;
    }
}