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

package ru.art.http.server.filter;

import org.zalando.logbook.RawHttpResponse;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.http.mime.MimeType.valueOf;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;

public interface HtmlLogsFilter {
    static String replaceWebResponseBody(RawHttpResponse response) {
        //Attention! Dont change this to EMPTY_STRING or something like it. Logbook check filter result and if it null, then filter is not applying.
        return getOrElse(httpServerModule()
                .getWebConfiguration()
                .getLogbookResponseBodyReplacers()
                .get(valueOf(response.getContentType())), null);
    }
}
