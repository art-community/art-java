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

package ru.art.generator.spec.http.proxyspec.constants;

import ru.art.generator.spec.http.proxyspec.operations.*;

/**
 * Interface containing constants for {@link HttpProxySpecificationClassGenerator}
 */
public interface HttpProxySpecConstants {
    String PROXY_SPEC = "ProxySpec";
    String UNWRAP = "unwrap";
    String HTTP_CLIENT_PROXY_BY_URL_WITHOUT_PARAMS = "httpClientProxy(getUrl() + $S)";
    String HTTP_CLIENT_PROXY_BY_URL_WITH_PARAMS = "HttpClientProxyBuilder.<$L, $L> httpClientProxy(getUrl() + $S)";

    String EXEC_REQ_PROXY_SPEC = "case $L:\nreturn $N().withBody(($T) request).prepare().execute();\nreturn null";
    String EXEC_RESP_PROXY_SPEC = "case $L:\nreturn cast(unwrap($N().execute()))";
    String EXEC_RESP_AND_REQ_PROXY_SPEC = "case $L:\nreturn cast(unwrap($N().withBody(($T) request).prepare().execute()))";
    String EXEC_NO_RESP_OR_REQ_PROXY_SPEC = "case $L:\n$N().execute();\nreturn null";

    String TODO_NOT_GENERATED_REQ_IN_EXEC = "//TODO add request manually, change method's type to \"HttpClientProxyWithRequestBodyBuilder\" and add \".withBody()\" method in ";

    interface ErrorConstants {
        String METHOD_PATH_IS_EMPTY = "Method path in @MethodPath annotation is empty!";
        String UNABLE_TO_DEFINE_MIME_TYPE_METHOD = "Unable to define MimeToContentTypeMapper method for HttpMimeType {0}.";
        String UNABLE_TO_DEFINE_MIME_TYPE = "Unable to define HttpMimeType: {0}.";
        String PARAM_FROM_IS_MISSING = ".withReq() parameter was not generated because annotation is missing: @FromBody";
    }
}
