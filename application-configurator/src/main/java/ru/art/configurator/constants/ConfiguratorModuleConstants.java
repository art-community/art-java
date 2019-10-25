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

package ru.art.configurator.constants;

import static java.util.stream.Collectors.*;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.constants.HttpCommonConstants.*;
import java.util.*;

public interface ConfiguratorModuleConstants {
    String CONFIGURATOR_MODULE_ID = "CONFIGURATOR_MODULE";

    String GRPC_SERVER_PORT_KEY = "grpc.server.port";
    String GRPC_SERVER_PATH_KEY = "grpc.server.path";

    String CONFIGURATOR_INDEX_HTML = "configurator.index.html";

    String TOKEN_COOKIE = "TOKEN";
    String LOGIN_PATH = API_PATH + "/login";
    String CHECK_TOKEN_PATH = API_PATH + "/checkToken";
    Set<String> AUTHORIZATION_CHECKING_PATHS = setOf("/get",
            "/getApplicationConfiguration",
            "/apply",
            "/profiles",
            "/modules",
            "/uploadApplication",
            "/uploadProfile",
            "/uploadModule")
            .stream()
            .map(path -> CONFIGURATOR_PATH + API_PATH + path)
            .collect(toSet());
    String UPLOAD_PATH = API_PATH + "/upload";
    String GET_PATH = API_PATH + "/get";
    String GET_APPLICATION_CONFIGURATION_PATH = API_PATH + "/getApplicationConfiguration";
    String APPLY_PATH = API_PATH + "/apply";
    String PROFILES_PATH = API_PATH + "/profiles";
    String MODULES_PATH = API_PATH + "/modules";
    String UPLOAD_APPLICATION_PATH = API_PATH + "/uploadApplication";
    String UPLOAD_PROFILE_PATH = API_PATH + "/uploadProfile";
    String UPLOAD_MODULE_PATH = API_PATH + "/uploadModule";
    String DELETE_MODULE_PATH = API_PATH + "/deleteModule";
    String DELETE_PROFILE_PATH = API_PATH + "/deleteProfile";

    interface ConfiguratorLocalConfigKeys {
        String CONFIGURATOR_SECTION_ID = "configurator";
        String CONFIGURATOR_HTTP_PORT_PROPERTY = "http.port";
        String CONFIGURATOR_HTTP_PATH_PROPERTY = "http.path";
        String CONFIGURATOR_GRPC_PORT_PROPERTY = "grpc.port";
        String CONFIGURATOR_GRPC_PATH_PROPERTY = "grpc.path";
        String CONFIGURATOR_USERNAME = "username";
        String CONFIGURATOR_PASSWORD = "password";
        String CONFIGURATOR_ROCKS_DB_PATH = "rocks.db.path";
    }
}
