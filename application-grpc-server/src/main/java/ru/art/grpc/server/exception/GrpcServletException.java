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

package ru.art.grpc.server.exception;

import io.grpc.*;
import io.grpc.Metadata.*;
import static io.grpc.Metadata.*;
import static io.grpc.Metadata.Key.*;
import static io.grpc.Status.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;

public class GrpcServletException extends StatusRuntimeException {
    public GrpcServletException(String message) {
        super(INTERNAL, buildMetadata(message));
    }

    private static Metadata buildMetadata(String message) {
        Metadata metadata = new Metadata();
        Key<String> key = of(PROTOBUF_ERROR_MESSAGE, ASCII_STRING_MARSHALLER);
        metadata.put(key, message);
        return metadata;
    }
}
