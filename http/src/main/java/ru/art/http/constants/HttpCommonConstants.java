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

public interface HttpCommonConstants {
    String API_PATH = "/api";
    String WEB_UI_PATH = "/ui";
    String WEB_UI_PATH_VARIABLE = "webUiPath";
    String API_PATH_VARIABLE = "apiPath";
    String HTTP_SCHEME = "http";
    String HTTPS_SCHEME = "https";
    int DEFAULT_HTTP_PORT = 80;
    int DEFAULT_HTTPS_PORT = 443;
}