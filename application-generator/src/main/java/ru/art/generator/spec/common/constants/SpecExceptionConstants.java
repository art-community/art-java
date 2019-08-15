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

package ru.art.generator.spec.common.constants;

/**
 * Interface for constants for exceptions occurred in all specification's generators.
 */
public interface SpecExceptionConstants {

    interface SpecificationGeneratorExceptions {
        String UNABLE_TO_GENERATE_SPECIFICATION = "Unable to generate specification for class ''{0}'' because of the ''{1}''.";
        String MAIN_ANNOTATION_ABSENT = "Annotation @{0} is not present for class ''{1}''. Specification was not generated.";
        String SERVICE_MARKED_IS_NON_GENERATED = "Service ''{0}'' is marked with @NonGenerated annotation and will not be generated.";
    }

    interface DefinitionExceptions {
        String UNABLE_TO_DEFINE_METHOD = "Unable to define method ''{0}'' in class ''{1}''.";
        String UNABLE_TO_DEFINE_ANNOTATION = "Unable to define annotation @{0}.";
        String UNABLE_TO_DEFINE_SPECIFICATION_TYPE = "Unable to define specification type ''{0}''.";
        String UNABLE_TO_DEFINE_NECESSITY_OF_IMPORT = "Unable to define if import is necessary because of exception: ";
    }
}
