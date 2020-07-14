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

package io.art.service.factory;

import lombok.experimental.*;
import io.art.service.exception.*;
import io.art.service.model.*;

@UtilityClass
public class ServiceResponseFactory {
    public static <T> ServiceResponse<T> okResponse(ServiceMethodCommand command) {
        return ServiceResponse.<T>builder().command(command).build();
    }

    public static <T> ServiceResponse<T> errorResponse(String errorCode, String errorMessage) {
        return ServiceResponse.<T>builder().serviceException(new ServiceExecutionException(errorCode, errorMessage)).build();
    }

    public static <T> ServiceResponse<T> errorResponse(String errorCode, Throwable throwable) {
        return ServiceResponse.<T>builder().serviceException(new ServiceExecutionException(errorCode, throwable)).build();
    }

    public static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, String errorCode, String errorMessage) {
        return ServiceResponse.<T>builder().command(command).serviceException(new ServiceExecutionException(command, errorCode, errorMessage)).build();
    }

    public static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, ServiceExecutionException exception) {
        return ServiceResponse.<T>builder().command(command).serviceException(exception).build();
    }

    public static <T> ServiceResponse<T> errorResponse(ServiceMethodCommand command, String errorCode, Throwable throwable) {
        return ServiceResponse.<T>builder().command(command).serviceException(new ServiceExecutionException(command, errorCode, throwable)).build();
    }

    public static <T> ServiceResponse<T> okResponse(ServiceMethodCommand command, T responseData) {
        return ServiceResponse.<T>builder().command(command).responseData(responseData).build();
    }
}
