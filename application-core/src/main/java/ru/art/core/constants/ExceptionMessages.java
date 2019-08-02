/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.core.constants;

public interface ExceptionMessages {
    String EXCEPTION_WRAPPER_ACTION_IS_NULL = "Exception wrapped action is null";
    String EXCEPTION_WRAPPER_FACTORY_IS_NULL = "ExceptionFactory is null";
    String CONTEXT_INITIAL_CONFIGURATION_IS_NULL = "ContextInitialConfiguration is null";
    String MODULE_ID_IS_NULL = "ModuleId is null";
    String MODULE_NOT_LOADED = "Module ''{0}'' was not loaded";
    String MODULE_HAS_NOT_STATE = "Module ''{0}'' hasn't state";
    String CUSTOM_MODULE_CONFIGURATION_IS_NULL = "CustomModuleConfiguration is null";
    String BUILDER_VALIDATOR_HAS_NEXT_ERRORS = "Builder validator has next error fields:";
    String COULD_NOT_FIND_AVAILABLE_PORT_AFTER_ATTEMPTS = "Could not find an available %s port in the range [%d, %d] after %d attempts";
    String COULD_NOT_FIND_AVAILABLE_PORTS_IN_THE_RANGE = "Could not find %d available %s ports in the range [%d, %d]";
}
