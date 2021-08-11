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

package io.art.communicator.constants;

public interface CommunicatorConstants {
    interface Errors {
        String COMMUNICATOR_WAS_NOT_REGISTERED = "Communicator with {0} was not registered";
        String COMMUNICATOR_HAS_INVALID_METHOD_FOR_PROXY = "Communicator interface {0} has methods:\n{1} with >1 parameters. Proxy could be created only for <2 parameter methods interfaces";
        String CONNECTOR_HAS_INVALID_METHOD_FOR_PROXY = "Connector interface {0} has methods:\n{1} with return type that is not proxy type";
    }

    interface LoggingMessages {
        String COMMUNICATOR_LOGGER = "communicator";
        String COMMUNICATOR_REGISTRATION_MESSAGE = "Registered communicator: ''{0}'' with actions: {1}";
        String COMMUNICATOR_SUBSCRIBED_MESSAGE = "Communicator subscribed: ''{0}.{1}''";
        String COMMUNICATOR_INPUT_DATA = "Communicator ''{0}.{1}'' input:\n{2}";
        String COMMUNICATOR_OUTPUT_DATA = "Communicator ''{0}.{1}'' output:\n{2}";
        String COMMUNICATOR_COMPLETED_MESSAGE = "Communicator completed: ''{0}.{1}''";
        String COMMUNICATOR_FAILED_MESSAGE = "Communicator failed: ''{0}.{1}''";
    }

    interface ConfigurationKeys {
        String COMMUNICATOR_SECTION = "communicator";
        String TARGETS_SECTION = "targets";
        String ACTIONS_SECTION = "actions";
        String LOGGING_KEY = "logging";
        String DEACTIVATED_KEY = "deactivated";
        String CONNECTOR_KEY = "connector";
    }

    interface CommunicatorProtocol {
        String getProtocol();

        String name();
    }
}
