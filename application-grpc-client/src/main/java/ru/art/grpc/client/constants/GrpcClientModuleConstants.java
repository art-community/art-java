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

public interface GrpcClientModuleConstants {
    String GRPC_COMMUNICATION_SERVICE_TYPE = "GRPC_COMMUNICATION";
    String GRPC_CLIENT_MODULE_ID = "GRPC_CLIENT_MODULE";
    String RESPONSE_OK = "OK";
    String TRACE_ID_HEADER = "TRACE_ID";
    long DEFAULT_TIMEOUT = 10000L;
    int DEFAULT_GRPC_PORT = 8000;
}
