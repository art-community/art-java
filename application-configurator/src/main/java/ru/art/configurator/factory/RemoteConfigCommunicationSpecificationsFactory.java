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

package ru.art.configurator.factory;

import lombok.SneakyThrows;
import org.apache.http.client.utils.URIBuilder;
import ru.art.config.remote.api.specification.RemoteConfigCommunicationSpecification;
import ru.art.configurator.api.entity.ModuleKey;
import ru.art.configurator.provider.ApplicationModulesParametersProvider.ApplicationModuleParameters;
import static java.lang.Integer.valueOf;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.GRPC_INSTANCES;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.entity.Value.asCollection;
import static ru.art.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.art.http.constants.HttpCommonConstants.HTTP_SCHEME;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import java.util.Set;

public interface RemoteConfigCommunicationSpecificationsFactory {
    @SneakyThrows
    static Set<RemoteConfigCommunicationSpecification> createRemoteConfigProxySpecs(ApplicationModuleParameters parameters, ModuleKey moduleKey) {
        URIBuilder uriBuilder = new URIBuilder();
        String url = uriBuilder
                .setScheme(HTTP_SCHEME)
                .setHost(parameters.getBalancerHost())
                .setPort(parameters.getBalancerPort())
                .setPath(moduleKey.getModuleId().replaceAll(DASH, UNDERSCORE) + DOT + moduleKey.getProfileId().replaceAll(DASH, UNDERSCORE) + GRPC_INSTANCES)
                .build()
                .toString();
        return httpCommunicator(url)
                .responseMapper(hosts -> asCollection(hosts).getStringSet())
                .consumes(applicationJsonUtf8())
                .get()
                .<Set<String>>execute()
                .orElse(emptySet())
                .stream()
                .map(host -> new RemoteConfigCommunicationSpecification(host.split(COLON)[0], valueOf(host.split(COLON)[1]), parameters.getPath()))
                .collect(toSet());
    }
}
