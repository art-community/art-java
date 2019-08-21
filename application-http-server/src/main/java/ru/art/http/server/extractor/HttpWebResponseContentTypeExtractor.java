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

package ru.art.http.server.extractor;


import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.http.constants.HttpMimeTypes.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.ResourceExtensions.*;

public interface HttpWebResponseContentTypeExtractor {
    static String extractTypeByFile(String fileUrl) {
        if (isEmpty(fileUrl)) return ALL.toString();
        if (!fileUrl.contains(DOT)) return TEXT_HTML.toString();
        String ext = fileUrl.substring(fileUrl.indexOf(DOT));
        if (ext.contains(WEBP)) {
            return IMAGE_WEBP.toString();
        }
        if (ext.contains(JPEG)) {
            return IMAGE_JPEG.toString();
        }
        if (ext.contains(PNG)) {
            return IMAGE_PNG.toString();
        }
        if (ext.contains(CSS)) {
            return TEXT_CSS.toString();
        }
        if (ext.contains(MAP)) {
            return TEXT_JS.toString();
        }
        if (ext.contains(JS)) {
            return TEXT_JS.toString();
        }
        if (ext.contains(HTML)) {
            return TEXT_HTML.toString();
        }
        return ALL.toString();
    }
}
