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

package ru.art.example.api.handler;

import ru.art.example.api.model.*;
import ru.art.service.model.*;
import java.util.*;

import static ru.art.logging.LoggingModule.*;


/**
 * handlers are needed to do something when asynchronous service execution failed or executed successfully
 */
public interface ExampleServiceHandlers {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handleRequestResponseHandlingExampleCompletion(Optional<ExampleRequest> request, ServiceResponse<ExampleResponse> response) {
        loggingModule().getLogger().info("SERVICE METHOD EXECUTED SUCCESSFULLY WITH REQUEST: " + request.toString());
        loggingModule().getLogger().info("SERVICE METHOD EXECUTED SUCCESSFULLY WITH RESPONSE: " + response.toString());
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handleRequestResponseHandlingExampleError(Optional<ExampleRequest> request, Throwable exception) {
        loggingModule().getLogger().info("SERVICE METHOD FAILED WITH ERROR: " + exception.toString());
        loggingModule().getLogger().info("SERVICE METHOD FAILED WITH REQUEST: " + request.toString());
    }
}
