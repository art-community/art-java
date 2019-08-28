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

package ru.art.service.interceptor;

import lombok.*;
import ru.art.service.constants.*;
import ru.art.service.exception.*;
import static java.util.Objects.*;

public interface ServiceExecutionInterceptor {
    static RequestInterceptor interceptRequest(ServiceRequestInterception interception) {
        if (isNull(interception)) throw new ServiceInternalException(ServiceExceptionsMessages.INTERCEPTION_IS_NULL);
        return new RequestInterceptor(interception);
    }

    static ResponseInterceptor interceptResponse(ServiceResponseInterception interception) {
        if (isNull(interception)) throw new ServiceInternalException(ServiceExceptionsMessages.INTERCEPTION_IS_NULL);
        return new ResponseInterceptor(interception);
    }

    @Getter
    @AllArgsConstructor
    class RequestInterceptor {
        private final ServiceRequestInterception interception;
    }

    @Getter
    @AllArgsConstructor
    class ResponseInterceptor {
        private final ServiceResponseInterception interception;
    }
}
