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

package ru.art.http.client.model;

import lombok.*;
import lombok.experimental.*;
import org.apache.http.client.config.*;

import static ru.art.http.client.module.HttpClientModule.*;
import static ru.art.http.constants.HttpCommonConstants.*;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class HttpCommunicationTargetConfiguration {
    @Setter
    @Accessors(fluent = true)
    private String url;
    @Setter
    private String path;
    @Builder.Default
    private final String host = httpClientModule().getBalancerHost();
    @Builder.Default
    private final Integer port = httpClientModule().getBalancerPort();
    @Builder.Default
    private final RequestConfig requestConfig = httpClientModule().getRequestConfig();
    @Builder.Default
    private final String scheme = HTTP_SCHEME;

    public HttpCommunicationTargetConfiguration addPath(String path) {
        this.path = this.path + path;
        return this;
    }
}
