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

import ru.art.service.constants.*;

import static java.text.MessageFormat.*;

public class UnknownServiceMethodException extends RuntimeException {
    public UnknownServiceMethodException(String serviceId, String methodId) {
        super(format(ServiceExceptionsMessages.UNKNOWN_SERVICE_METHOD_MESSAGE, serviceId, methodId));
    }
}
