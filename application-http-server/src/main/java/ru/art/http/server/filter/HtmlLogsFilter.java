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
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.http.constants.HttpMimeTypes.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.WEB_RESOURCE;

public interface HtmlLogsFilter {
    static String replaceWebResponseBody(RawHttpResponse response) {
        if (TEXT_HTML.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (TEXT_JS.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (TEXT_CSS.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_WEBP.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_PNG.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_JPEG.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_GIF.toString().equals(response.getContentType())) return WEB_RESOURCE;
        return EMPTY_STRING;
    }
}
