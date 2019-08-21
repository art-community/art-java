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

import java.util.*;

import static ru.art.core.factory.CollectionsFactory.*;

public interface ConfiguratorModuleConstants {
    String CONFIGURATOR_MODULE_ID = "CONFIGURATOR_MODULE";
    String GRPC_INSTANCES = ".grpc.instances";

    String APPLICATION_BALANCER_HOST_CONFIG_KEY = "balancer.host";
    String APPLICATION_BALANCER_PORT_CONFIG_KEY = "balancer.port";
    String APPLICATION_MODULE_GRPC_PATH_CONFIG_KEY = "grpc.server.path";
    String TOKEN_COOKIE = "TOKEN";
    String LOGIN_PATH = "/login";
    String CHECK_TOKEN_PATH = "/checkToken";
    Set<String> AUTHORIZATION_CHECKING_URLS = setOf("/upload",
            "/get",
            "/getApplicationConfiguration",
            "/apply",
            "/profiles",
            "/modules",
            "/uploadApplication",
            "/uploadProfile",
            "/uploadModule");
    String UPLOAD_PATH = "/upload";
    String GET_PATH = "/get";
    String GET_APPLICATION_CONFIGURATION_PATH = "/getApplicationConfiguration";
    String APPLY_PATH = "/apply";
    String PROFILES_PATH = "/profiles";
    String MODULES_PATH = "/modules";
    String UPLOAD_APPLICATION_PATH = "/uploadApplication";
    String UPLOAD_PROFILE_PATH = "/uploadProfile";
    String UPLOAD_MODULE_PATH = "/uploadModule";

    interface ConfiguratorLocalConfigKeys {
        String CONFIGURATOR_SECTION_ID = "configurator";
        String CONFIGURATOR_HTTP_PORT_PROPERTY = "http.port";
        String CONFIGURATOR_HTTP_PATH_PROPERTY = "http.path";
        String CONFIGURATOR_GRPC_PORT_PROPERTY = "grpc.port";
        String CONFIGURATOR_GRPC_PATH_PROPERTY = "grpc.path";
        String CONFIGURATOR_URL_PROPERTY = "http.url";
        String CONFIGURATOR_USERNAME = "username";
        String CONFIGURATOR_PASSWORD = "password";
        String CONFIGURATOR_ROCKS_DB_PATH = "rocks.db.path";
    }
}
