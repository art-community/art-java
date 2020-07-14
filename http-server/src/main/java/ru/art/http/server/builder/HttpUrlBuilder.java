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

package ru.art.http.server.builder;

import lombok.*;
import static ru.art.core.constants.CharConstants.COLON;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.http.constants.HttpCommonConstants.*;

public interface HttpUrlBuilder {
    static String buildUrl(UrlInfo info) {
        StringBuilder url = new StringBuilder();
        int port = info.port;
        if (port < 0) {
            // Work around java.net.URL bug
            port = DEFAULT_HTTP_PORT;
        }

        String scheme = info.scheme;
        url.append(scheme);
        url.append(SCHEME_DELIMITER);
        url.append(info.serverName);
        url.append(COLON);
        url.append(port);
        url.append(info.uri);
        return url.toString();
    }

    @Getter
    @Builder
    class UrlInfo {
        private int port;
        private String scheme;
        private String serverName;
        private String uri;
    }
}
