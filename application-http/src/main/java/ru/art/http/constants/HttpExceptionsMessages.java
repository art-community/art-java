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

package ru.art.http.constants;

public interface HttpExceptionsMessages {
    String CONTENT_TYPE_IS_NULL = "Content type is null";
    String REQUEST_INTERCEPTOR_IS_NULL = "Request interceptor is null";
    String RESPONSE_INTERCEPTOR_IS_NULL = "Response interceptor is null";
    String REQUEST_CONTENT_TYPE_IS_NULL = "Request content type is null";
    String RESPONSE_CONTENT_TYPE_IS_NULL = "Response content type is null";
    String RESPONSE_MAPPER_IS_NULL = "Response mapper is null";
    String EXCEPTION_MAPPER_IS_NULL = "Exception mapper is null";
    String REQUEST_MAPPER_IS_NULL = "Request mapper is null";
    String VALIDATION_POLICY_IS_NULL = "Validation policy is null";
    String REQUEST_VALUE_INTERCEPTOR = "Request value interceptor policy is null";
    String RESPONSE_VALUE_INTERCEPTOR = "Response value interceptor policy is null";
    String EXCEPTION_VALUE_INTERCEPTOR = "Exception value interceptor policy is null";
}
