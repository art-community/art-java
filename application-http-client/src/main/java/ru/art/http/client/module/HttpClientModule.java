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

package ru.art.http.client.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.http.client.configuration.HttpClientModuleConfiguration;
import static ru.art.core.context.Context.context;
import static ru.art.http.client.configuration.HttpClientModuleConfiguration.HttpClientModuleDefaultConfiguration;
import static ru.art.http.client.constants.HttpClientModuleConstants.HTTP_CLIENT_MODULE_ID;

@Getter
public class HttpClientModule implements Module<HttpClientModuleConfiguration, ModuleState> {
    private final HttpClientModuleConfiguration defaultConfiguration = new HttpClientModuleDefaultConfiguration();
    private final String id = HTTP_CLIENT_MODULE_ID;

    public static HttpClientModuleConfiguration httpClientModule() {
        return context().getModule(HTTP_CLIENT_MODULE_ID, new HttpClientModule());
    }
}
