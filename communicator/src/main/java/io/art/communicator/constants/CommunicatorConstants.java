/*
 * ART
 *
 * Copyright 2019-2022 ART
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
        String COMMUNICATOR_HAS_INVALID_METHODS = "Communicator interface {0} should not contain methods with more than one parameter:\n[{1}]";
        String CONNECTOR_HAS_INVALID_METHODS = "Connector interface {0} should not contain methods that returning not Communicator implementations:\n[{1}]";
    }

    interface LoggingMessages {
        String COMMUNICATOR_LOGGER = "communicator";
        String COMMUNICATOR_SUBSCRIBED_MESSAGE = "Communicator subscribed: ''{0}.{1}''";
        String COMMUNICATOR_INPUT_DATA = "Communicator ''{0}.{1}'' input:\n{2}";
        String COMMUNICATOR_OUTPUT_DATA = "Communicator ''{0}.{1}'' output:\n{2}";
        String COMMUNICATOR_COMPLETED_MESSAGE = "Communicator completed: ''{0}.{1}''";
        String COMMUNICATOR_FAILED_MESSAGE = "Communicator failed: ''{0}.{1}''";
    }

    interface ConfigurationKeys {
        String CONNECTORS_SECTION = "connectors";
        String COMMUNICATOR_SECTION = "communicator";
        String CLIENTS_SECTION = "clients";
        String ACTIONS_SECTION = "actions";
    }
}
