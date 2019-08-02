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

package ru.art.service.factory;

import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;

public interface ServiceResponseFactory {
    static <T> ServiceResponse<T> okResponse(ServiceMethodCommand command) {
        return ServiceResponse.<T>builder().command(command).build();
    }

    static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, String errorCode, String errorMessage) {
        return ServiceResponse.<T>builder().command(command).serviceException(new ServiceExecutionException(command, errorCode, errorMessage)).build();
    }

    static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, ServiceExecutionException exception) {
        return ServiceResponse.<T>builder().command(command).serviceException(exception).build();
    }

    static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, String errorCode, Exception e) {
        return ServiceResponse.<T>builder().command(command).serviceException(new ServiceExecutionException(command, errorCode, e)).build();
    }

    static <T> ServiceResponse<T> okResponse(ServiceMethodCommand command, T responseData) {
        return ServiceResponse.<T>builder().command(command).responseData(responseData).build();
    }
}
