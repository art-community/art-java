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

package io.art.rsocket.configuration;

import io.art.core.module.*;
import io.art.core.source.*;
import io.art.entity.constants.EntityConstants.*;
import io.art.server.model.*;
import lombok.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.parser.EnumParser.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import java.time.*;

@Getter
public class RsocketModuleConfiguration implements ModuleConfiguration {
    private DataFormat defaultDataFormat = MESSAGE_PACK;
    private ServiceMethodIdentifier defaultServiceMethod;
    private boolean tracing;
    private int fragmentationMtu;
    private RsocketResumeConfiguration resume;
    private String host;
    private int tcpPort;
    private int webSocketPort;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<RsocketModuleConfiguration, Configurator> {
        private final RsocketModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            String serviceId = source.getString(RSOCKET_SERVER_DEFAULT_SERVICE_ID_KEY);
            String methodId = source.getString(RSOCKET_SERVER_DEFAULT_METHOD_ID_KEY);
            if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
                configuration.defaultServiceMethod = serviceMethod(serviceId, methodId);
            }

            configuration.defaultDataFormat = enumOf(DataFormat::valueOf, source.getString(RSOCKET_DEFAULT_SERVER_DATA_FORMAT_KEY), JSON);
            configuration.tracing = orElse(source.getBool(RSOCKET_SERVER_TRACING_KEY), false);
            configuration.fragmentationMtu = orElse(source.getInt(RSOCKET_SERVER_FRAGMENTATION_MTU_KEY), 0);

            if (source.has(RSOCKET_RESUME_SECTION)) {
                boolean cleanupStoreOnKeepAlive = orElse(source.getBool(RSOCKET_RESUME_CLEANUP_STORE_ON_KEEP_ALIVE), false);
                Duration sessionDuration = source.getDuration(RSOCKET_RESUME_SESSION_DURATION);
                Duration streamTimeout = source.getDuration(RSOCKET_RESUME_STREAM_TIMEOUT);
                RetryPolicy retryPolicy = enumOf(RetryPolicy::valueOf, source.getString(RSOCKET_RESUME_RETRY_POLICY), BACKOFF);
                configuration.resume = RsocketResumeConfiguration.builder()
                        .sessionDuration(sessionDuration)
                        .streamTimeout(streamTimeout)
                        .cleanupStoreOnKeepAlive(cleanupStoreOnKeepAlive)
                        .retryPolicy(retryPolicy)
                        .build();
            }

            return this;
        }
    }

}
