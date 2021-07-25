/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.server.constants;

public interface ServerModuleConstants {
    interface Defaults {
    }

    interface ConfigurationKeys {
        String SERVER_SECTION = "server";
        String SERVER_SERVICES_KEY = "services";
        String DEACTIVATED_KEY = "deactivated";
        String LOGGING_KEY = "logging";
        String VALIDATING_KEY = "validating";
        String METHODS_KEY = "methods";
    }


    interface LoggingMessages {
        String SERVICE_REGISTRATION_MESSAGE = "Registered service methods: {0}";
        String SERVICE_SUBSCRIBED_MESSAGE = "Service subscribed: ''{0}.{1}''";
        String SERVICE_INPUT_DATA = "Service ''{0}.{1}'' input:\n{2}";
        String SERVICE_OUTPUT_DATA = "Service ''{0}.{1}'' output:\n{2}";
        String SERVICE_COMPLETED_MESSAGE = "Service completed: ''{0}.{1}''";
        String SERVICE_FAILED_MESSAGE = "Service failed: ''{0}.{1}''";
    }

    interface ExceptionMessages {
    }

    interface StateKeys {
        String SERVICE_METHOD_ID = "serviceMethodId";
    }
}
