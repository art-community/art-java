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

package ru.art.grpc.client.exception;

import lombok.Getter;
import ru.art.grpc.client.communicator.GrpcCommunicationConfiguration;
import static java.text.MessageFormat.format;
import static ru.art.grpc.client.constants.GrpcClientExceptionMessages.GRPC_CLIENT_EXCEPTION_MESSAGE;

@Getter
public class GrpcResponseException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public GrpcResponseException(GrpcCommunicationConfiguration configuration, String errorCode, String message) {
        super(format(GRPC_CLIENT_EXCEPTION_MESSAGE, configuration, errorCode, message));
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
}
