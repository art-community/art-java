/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.configuration;

import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.http.constants.HttpModuleConstants.ConfigurationKeys.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class HttpServiceConfiguration {
    private ImmutableMap<String, HttpMethodConfiguration> methods;
    private String path;


    public static HttpServiceConfiguration from(ConfigurationSource source) {
        HttpServiceConfiguration configuration = HttpServiceConfiguration.builder().build();
        configuration.methods = source.getNestedMap(METHODS_KEY, method -> HttpMethodConfiguration.from(configuration, method));
        configuration.path = source.getString(PATH_KEY);
        return configuration;
    }
}
