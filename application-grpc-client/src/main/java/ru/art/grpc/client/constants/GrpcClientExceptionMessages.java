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

package ru.art.grpc.client.constants;

public interface GrpcClientExceptionMessages {
    String RESPONSE_IS_NULL = "Received response is null";
    String GRPC_CLIENT_EXCEPTION_MESSAGE = "Exception occurred from external module. Please, check logs. GRPC communication configuration: {0}. Error code: {1}. ErrorMessage: {2}";
    String GRPC_COMMUNICATION_TARGET_CONFIGURATION_NOT_FOUND = "GRPC communication target configuration for service ''{0}'' was not found";
    String GRPC_RESPONSE_MAPPING_MODE_IS_NULL = "GRPC response mapping mode is null";
    String INVALID_GRPC_COMMUNICATION_CONFIGURATION = "Some required fields in GRPC communication configuration are null: ";
}
