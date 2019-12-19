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

public interface ServiceExceptionsMessages {
    String INTERCEPTION_IS_NULL = "Interception is null";
    String SERVICE_EXECUTION_EXCEPTION_MESSAGE = "Service ''{0}'' method ''{1}'' execution failed with errorCode ''{2}'' and error message: ''{3}''";
    String SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE = "Service ''{0}'' method ''{1}'' execution failed with errorCode ''{2}'', error message: ''{3}'' and stackTrace\n{4}";
    String SERVICE_EXECUTION_EXCEPTION_MESSAGE_AND_STACKTRACE_WITHOUT_COMMAND = "Service execution failed with errorCode ''{1}'', error message: ''{2}'' and stackTrace\n{3}";
    String UNKNOWN_SERVICE_METHOD_MESSAGE = "Service ''{0}'' has not method with id: ''{1}''";
    String REQUEST_DATA_IS_NULL = "RequestData is null";
    String SERVICE_COMMAND_HAS_WRONG_FORMAT = "Service command has wrong format (SERVICE_ID.METHOD_ID()): ''{0}''";
    String THROWABLE_EXCEPTION_WRAPPER_IS_NULL = "ThrowableExceptionWrapper is null";
    String WRAPPER_IS_NULL = "Wrapper is null";
    String NOT_BETWEEN_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' not between ''{2,number,#}'' and ''{3,number,#}''";
    String NOT_EQUALS_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' is not equals to ''{2}''";
    String NOT_CONTAINS_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' is not contains to ''{2}''";
    String EQUALS_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' is equals to ''{2}''";
    String EMPTY_VALIDATION_ERROR = "Validation error. ''{0}'' is empty";
    String NULL_VALIDATION_ERROR = "Validation error. ''{0}'' is null";
    String SERVICE_WITH_ID_NOT_EXISTS = "Service with id ''{0}'' not exists";
    String SERVICE_COMMAND_IS_NULL = "Service command is null";
    String SERVICE_ID_IS_NULL = "Service id is null";
    String METHOD_ID_IS_NULL = "Method id is null";
    String ERROR_CODE_IS_EMPTY = "Error code is empty";
    String REQUEST_DATA_IS_NULL_CODE = "REQUEST_DATA_IS_NULL";
    String VALIDATION_EXCEPTION_CODE = "VALIDATION_EXCEPTION";
}
