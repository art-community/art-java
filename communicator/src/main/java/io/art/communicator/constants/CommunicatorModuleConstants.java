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

package io.art.communicator.constants;

public interface CommunicatorModuleConstants {
    interface Defaults {
    }

    interface ExceptionMessages {
        String COMMUNICATOR_WAS_NOT_REGISTERED = "Communicator with {0} was not registered";
    }

    interface LoggingMessages {
        String COMMUNICATOR_REGISTRATION_MESSAGE = "Registered communicator: ''{0}'' with actions: {1}";
        String COMMUNICATOR_SUBSCRIBED_MESSAGE = "Communicator subscribed: ''{0}.{1}''";
        String COMMUNICATOR_INPUT_DATA = "Communicator ''{0}.{1}'' input:\n{2}";
        String COMMUNICATOR_OUTPUT_DATA = "Communicator ''{0}.{1}'' output:\n{2}";
        String COMMUNICATOR_COMPLETED_MESSAGE = "Communicator completed: ''{0}.{1}''";
        String COMMUNICATOR_FAILED_MESSAGE = "Communicator failed: ''{0}.{1}''";
    }

    interface ConfigurationKeys {
        String COMMUNICATOR_SECTION = "communicator";
        String PROXIES_SECTION = "proxies";
        String ACTIONS_SECTION = "actions";
        String LOGGING_KEY = "logging";
        String DEACTIVATED_KEY = "deactivated";
        String CONNECTORS_KEY = "connectors";
    }

    interface CommunicatorProtocol {
        String getProtocol();

        String name();
    }
}
