/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.client.body.descriptor;

import org.apache.http.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.http.client.constants.HttpClientExceptionMessages.*;
import static io.art.http.client.module.HttpClientModule.*;
import static io.art.logging.LoggingModule.*;
import static java.util.Objects.*;

public interface HttpBodyDescriptor {
    static byte[] readResponseBody(HttpEntity responseEntity) {
        if (isNull(responseEntity)) return EMPTY_BYTES;
        try {
            return toByteArray(responseEntity.getContent(), httpClientModule().getResponseBodyBufferSize());
        } catch (Throwable throwable) {
            logger(HttpBodyDescriptor.class).error(REQUEST_BODY_READING_EXCEPTION, throwable);
            return EMPTY_BYTES;
        }
    }
}
