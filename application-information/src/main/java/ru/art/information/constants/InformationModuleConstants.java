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

package ru.art.information.constants;

import static ru.art.http.constants.HttpCommonConstants.API_PATH;

public interface InformationModuleConstants {
    String INFORMATION_PATH = "/information";
    String GET_INFORMATION_PATH = INFORMATION_PATH + API_PATH + "/get";
    String STATUS_PATH = "/status";
    String HTTP_SERVER_WAS_NOT_INITIALIZED = "HTTP Server was not initialized";
    String GRPC_SERVER_WAS_NOT_INITIALIZED = "GRPC Server was not initialized";
    String INFORMATION_MODULE_ID = "INFORMATION_MODULE";
    String INFORMATION_INDEX_HTML = "information.index.html";
    String MAIN_MODULE_ID_VARIABLE =  "mainModuleId";
}
