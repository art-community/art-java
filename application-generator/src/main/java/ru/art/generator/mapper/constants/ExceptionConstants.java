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

package ru.art.generator.mapper.constants;

/**
 * Interface for exception constants of mapper generator
 */
public interface ExceptionConstants {
    interface MapperGeneratorExceptions {
        String UNABLE_TO_CREATE_REQ_RES_MAPPER = "Unable to create Request/Response Mapper file ''{0}''";
        String UNABLE_TO_CREATE_MAPPER = "Unable to create Mapper file ''{0}''";
        String UNABLE_TO_GENERATE_INTERFACE = "Unable to generate interface for type ''{0}'' because of the ''{1}''";
        String UNABLE_TO_FIND_A_PATH_FOR_CLASS = "Unable to find a path for class ''{0}''. Mapper wasn't created.";
        String UNABLE_TO_CREATE_INNER_CLASS_MAPPER = "Unable to create inner class mapper, because inner class ''{0}'' is an enum or marked as NonGenerated.";
        String UNABLE_TO_PARSE_JAR_PATH = "Unable to parse jar path for ''{0}''";
        String UNABLE_TO_CREATE_MAPPER_UNKNOWN_ERROR = "Unable to create Mapper file ''{0}'' because of the ''{1}''";
    }

    interface DefinitionExceptions {
        String UNABLE_TO_DEFINE_GENERIC_TYPE = "Unable to define generic type ''{0}'' for variable ''{1}'' in class ''{2}'' for ''{3}''";
        String UNABLE_TO_DEFINE_CLASS = "Unable to define class ''{0}'' by url";
    }
}
