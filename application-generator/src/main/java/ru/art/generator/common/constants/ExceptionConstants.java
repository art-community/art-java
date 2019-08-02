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

package ru.art.generator.common.constants;

/**
 * Interface for common constants for exceptions occurred in generators.
 */
public interface ExceptionConstants {
    String UNABLE_TO_PARSE_JAR_PATH = "Unable to parse jar path for ''{0}''";
    String UNABLE_TO_WRITE_TO_FILE = "Unable to write to file ''{0}''";
    String UNABLE_TO_FIND_A_PATH_FOR_CLASS = "Unable to find a path for class ''{0}''. File wasn''t created.";
    String UNABLE_TO_CREATE_FILE_UNKNOWN_ERROR = "Unable to create file ''{0}'' because of the ''{1}''";
    String NOT_SUPPORTED_TYPE_FOR_PRIMITIVE_MAPPER = "''{0}'' - isn''t supported type for getting primitive mapper.";
}
