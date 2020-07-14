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

package ru.art.service.constants;

public interface ServiceLoggingMessages {
    String EXECUTION_SERVICE_MESSAGE = "Executing service: ''{0}.{1}'' with request: ''{2}''";
    String SERVICE_FAILED_MESSAGE = "Service ''{0}.{1}'' execution failed with error: ''{2}: {3}\n{4}''";
    String SERVICE_EXECUTED_MESSAGE = "Successfully executed service: ''{0}.{1}'' with response: ''{2}''";
    String SERVICE_REGISTRATION_MESSAGE = "Registering service: ''{0}'' with specification class: ''{1}''";
}
