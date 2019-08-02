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

package ru.art.generator.spec.http.common.constants;

/**
 * Interface for common constants for exceptions occurred in http specification's generators
 */
public interface HttpSpecConstants {

    interface Methods {
        String FROM_BODY_METHOD = ".fromBody()";
        String FROM_PATH_PARAMS_METHOD = ".fromPathParams()";
        String FROM_QUERY_PARAMS_METHOD = ".fromQueryParams()";
        String VALIDATION_POLICY_METHOD = ".validationPolicy()";
        String VALIDATION_POLICY = ".validationPolicy($N)";
        String GET = ".get($L)";
        String POST = ".post($L)";
        String PUT = ".put($L)";
        String PATCH = ".patch($L)";
        String OPTIONS = ".options($L)";
        String DELETE = ".delete($L)";
        String TRACE = ".trace($L)";
        String HEAD = ".head($L)";
        String CONNECT = ".connect($L)";
    }

    interface Errors {
        String INCOMPATIBLE_ANNOTATIONS = "Method ''{0}'' was not generated because it has incompatible annotations: {1}";
        String NO_HTTP_METHODS_ANNOTATION = "Method ''{0}'' was not generated because it does not have http methods' annotations: @HttpPost/@HttpGet/@HttpPut/@HttpPatch/@HttpOptions/@HttpDelete/@HttpTrace/@HttpHead.";
        String PARAM_IS_MISSING = "{0} parameter was not generated because annotation is missing: @{1}";
        String UNABLE_TO_GENERATE_BECAUSE_OF_ANNOTATION = "Unable to generate {0} for @{1}";
        String METHOD_CONSUMES_WITHOUT_PARAMS = "Method has @HttpConsumes annotation, but does not have parameters.";
    }
}