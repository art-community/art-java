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

package ru.art.generator.spec.http.servicespec.constants;

import ru.art.generator.spec.http.servicespec.operations.*;

/**
 * Interface containing constants for {@link HttpServiceSpecificationClassGenerator}
 */
public interface HttpServiceSpecConstants {
    String SPEC = "Spec";
    String HTTP_SERVICE = "httpService";
    String HTTP_SERVICE_METHOD = "httpService()";

    interface Methods {
        String LISTEN_METHOD = ".listen()\n";
        String LISTEN_METHOD_FILLED = ".listen($S)\n";
        String SERVE_METHOD = ".serve($S)";
        String FROM_MULTIPART_METHOD = ".fromMultipart()";
    }

    interface ExceptionConstants {
        String UNABLE_TO_DEFINE_VALIDATION_ANNOTATION = "Unable to generate .withReq() parameter. Method has multiple annotation for validation policy: @Validatable, @NotNull.";
        String MULTIPLE_DEFAULT_LISTEN = "Default .listen() method was generated more than one time so it's impossible to define method's path to call. Add @Listen annotation with path and repeat generation or add it manually.";
        String PARAM_REQUEST_IS_MISSING = "{0} parameter wasn''t generated because annotation is missing: @RequestMapper";
        String PARAM_FROM_NOT_GENERATED_CAUSE_OF_VALIDATION = "{0} parameter wasn''t generated because of dependency of .withReq() block.";
        String PARAM_FROM_IS_MISSING = ".withReq() parameter wasn't generated because annotation is missing: @FromBody/@FromMultipart/@FromPathParams/@FromQueryParams";
    }
}
