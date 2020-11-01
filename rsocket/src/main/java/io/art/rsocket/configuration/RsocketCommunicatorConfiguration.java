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

import com.google.common.collect.*;
import io.art.core.source.*;
import io.art.server.model.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import lombok.*;
import reactor.util.retry.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.value.constants.EntityConstants.*;
import static io.art.value.constants.EntityConstants.DataFormat.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import static io.rsocket.frame.FrameLengthCodec.*;
import static java.util.Optional.*;

@Getter
public class RsocketCommunicatorConfiguration {
    private ImmutableMap<String, RsocketConnectorConfiguration> connectors;
    private boolean logging;
    private int fragmentationMtu;
    private Resume resume;
    private Retry reconnect;
    private PayloadDecoder payloadDecoder;
    private int maxInboundPayloadSize;
    private DataFormat defaultDataFormat;
    private DataFormat defaultMetaDataFormat;
    private ServiceMethodIdentifier defaultServiceMethod;
    private RsocketKeepAliveConfiguration keepAliveConfiguration;

    public static RsocketCommunicatorConfiguration from(ConfigurationSource source) {
        RsocketCommunicatorConfiguration configuration = new RsocketCommunicatorConfiguration();

        String serviceId = source.getString(DEFAULT_SERVICE_ID_KEY);
        String methodId = source.getString(DEFAULT_METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            configuration.defaultServiceMethod = serviceMethod(serviceId, methodId);
        }

        configuration.defaultDataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), JSON);
        configuration.defaultMetaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), JSON);
        configuration.logging = orElse(source.getBool(LOGGING_KEY), false);
        configuration.fragmentationMtu = orElse(source.getInt(FRAGMENTATION_MTU_KEY), 0);
        configuration.maxInboundPayloadSize = orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), FRAME_LENGTH_MASK);
        configuration.resume = let(source.getNested(RESUME_SECTION), RsocketResumeConfigurator::from);
        configuration.reconnect = let(source.getNested(RECONNECT_SECTION), RsocketRetryConfigurator::from);
        configuration.keepAliveConfiguration = let(source.getNested(KEEP_ALIVE_SECTION), RsocketKeepAliveConfiguration::from);
        configuration.payloadDecoder = rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY)) == DEFAULT
                ? PayloadDecoder.DEFAULT
                : PayloadDecoder.ZERO_COPY;

        configuration.connectors = ofNullable(source.getNestedMap(CONNECTORS_KEY))
                .map(configurations -> configurations.entrySet()
                        .stream()
                        .collect(toImmutableMap(Entry::getKey, entry -> RsocketConnectorConfiguration.from(configuration, entry.getValue()))))
                .orElse(ImmutableMap.of());

        return configuration;
    }
}
