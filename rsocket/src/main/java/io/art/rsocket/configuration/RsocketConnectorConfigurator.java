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

import io.art.core.source.*;
import io.art.entity.constants.EntityConstants.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.entity.mime.MimeTypeDataFormatMapper.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.server.model.ServiceMethodIdentifier.*;

@UtilityClass
public class RsocketConnectorConfigurator {
    public static RSocketConnector from(RsocketCommunicatorConfiguration communicatorConfiguration, ConfigurationSource source) {
        RSocketConnector connector = RSocketConnector.create();
        DataFormat dataFormat = dataFormat(source.getString(DEFAULT_DATA_FORMAT_KEY), communicatorConfiguration.getDefaultDataFormat());
        DataFormat metaDataFormat = dataFormat(source.getString(DEFAULT_META_DATA_FORMAT_KEY), communicatorConfiguration.getDefaultMetaDataFormat());
        boolean tracing = orElse(source.getBool(TRACING_KEY), communicatorConfiguration.isTracing());

        apply(source.getNested(RESUME_SECTION), section -> connector.resume(RsocketResumeConfigurator.from(section, communicatorConfiguration.getResume())));
        apply(source.getNested(RECONNECT_SECTION), section -> connector.reconnect(RsocketRetryConfigurator.from(section, communicatorConfiguration.getReconnect())));
        apply(
                let(source.getNested(KEEP_ALIVE_SECTION), section -> RsocketKeepAliveConfiguration.from(section, communicatorConfiguration.getKeepAliveConfiguration())),
                configuration -> connector.keepAlive(configuration.getInterval(), configuration.getMaxLifeTime())
        );

        connector.payloadDecoder(rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY)) == DEFAULT ? PayloadDecoder.DEFAULT : PayloadDecoder.ZERO_COPY)
                .fragment(orElse(source.getInt(FRAGMENTATION_MTU_KEY), communicatorConfiguration.getFragmentationMtu()))
                .maxInboundPayloadSize(orElse(source.getInt(MAX_INBOUND_PAYLOAD_SIZE_KEY), communicatorConfiguration.getMaxInboundPayloadSize()))
                .dataMimeType(toMimeType(dataFormat).toString())
                .metadataMimeType(toMimeType(metaDataFormat).toString())
                .interceptors(registry -> registry.forRequester(new RsocketLoggingInterceptor(() -> tracing)));

        RsocketSetupPayload.RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(dataFormat)
                .metadataFormat(metaDataFormat);

        String serviceId = source.getString(DEFAULT_SERVICE_ID_KEY);
        String methodId = source.getString(DEFAULT_METHOD_ID_KEY);

        if (isNotEmpty(serviceId) && isNotEmpty(methodId)) {
            setupPayloadBuilder.serviceMethodId(serviceMethod(serviceId, methodId));
        }

        RsocketPayloadWriter writer = new RsocketPayloadWriter(dataFormat, metaDataFormat);
        connector.setupPayload(writer.writePayloadData(setupPayloadBuilder.build().toEntity()));

        return connector;
    }
}
