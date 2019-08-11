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

package ru.art.configurator.api.constants;

public interface ConfiguratorServiceConstants {
    String DEFAULT_CONFIGURATOR_PATH = "/configurator";
    String CONFIGURATOR_SERVICE_ID = "CONFIGURATOR_SERVICE";
    String USER_SERVICE_ID = "USER_SERVICE";

    interface Methods {
        String LOGIN = "LOGIN";
        String CHECK_TOKEN = "CHECK_TOKEN";
        String UPLOAD_CONFIG = "UPLOAD_CONFIG";
        String GET_PROTOBUF_CONFIG = "GET_PROTOBUF_CONFIG";
        String GET_JSON_CONFIG = "GET_JSON_CONFIG";
        String APPLY_MODULE_CONFIG = "APPLY_MODULE_CONFIG";
        String GET_ALL_PROFILES = "GET_ALL_PROFILES";
        String GET_ALL_MODULES = "GET_ALL_MODULES";
        String UPLOAD_APPLICATION_CONFIG = "UPLOAD_APPLICATION_CONFIG";
        String UPLOAD_PROFILE_CONFIG = "UPLOAD_PROFILE_CONFIG";
        String UPLOAD_MODULE_CONFIG = "UPLOAD_MODULE_CONFIG";
        String GET_APPLICATION_CONFIG = "GET_APPLICATION_CONFIG";
    }
}
