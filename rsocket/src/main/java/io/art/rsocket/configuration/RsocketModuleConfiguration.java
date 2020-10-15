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
import io.art.rsocket.constants.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.server.model.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import lombok.*;
import reactor.netty.http.server.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.entity.constants.EntityConstants.DataFormat.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RetryPolicy.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.server.model.ServiceMethodIdentifier.*;
import static reactor.util.retry.Retry.*;
import java.time.*;

@Getter
public class RsocketModuleConfiguration implements ModuleConfiguration {
    private DataFormat defaultDataFormat = MESSAGE_PACK;
    private ServiceMethodIdentifier defaultServiceMethod;
    private boolean tracing;
    private int fragmentationMtu;
    private Resume resume;
    private PayloadDecoder payloadDecoder;
    private int maxInboundPayloadSize;
    private TcpServer tcpServer;
    private HttpServer httpWebSocketServer;
    private TransportMode transport;

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

            configuration.defaultDataFormat = dataFormat(source.getString(RSOCKET_DEFAULT_SERVER_DATA_FORMAT_KEY), JSON);
            configuration.tracing = orElse(source.getBool(RSOCKET_SERVER_TRACING_KEY), false);
            configuration.fragmentationMtu = orElse(source.getInt(RSOCKET_SERVER_FRAGMENTATION_MTU_KEY), 0);

            if (source.has(RSOCKET_SERVER_RESUME_SECTION)) {
                boolean cleanupStoreOnKeepAlive = orElse(source.getBool(RSOCKET_SERVER_RESUME_CLEANUP_STORE_ON_KEEP_ALIVE_KEY), false);
                Duration sessionDuration = orElse(source.getDuration(RSOCKET_SERVER_RESUME_SESSION_DURATION_KEY), DEFAULT_RESUME_SESSION_DURATION);
                Duration streamTimeout = orElse(source.getDuration(RSOCKET_SERVER_RESUME_STREAM_TIMEOUT_KEY), DEFAULT_RESUME_STREAM_TIMEOUT);
                configuration.resume = new Resume()
                        .streamTimeout(streamTimeout)
                        .sessionDuration(sessionDuration);
                if (cleanupStoreOnKeepAlive) {
                    configuration.resume.cleanupStoreOnKeepAlive();
                }
                if (source.has(RSOCKET_SERVER_RESUME_RETRY_POLICY_KEY)) {
                    RsocketModuleConstants.RetryPolicy retryPolicy = rsocketRetryPolicy(source.getString(RSOCKET_SERVER_RESUME_RETRY_POLICY_KEY));
                    switch (retryPolicy) {
                        case BACKOFF:
                            long maxAttempts = orElse(source.getLong(RSOCKET_SERVER_RESUME_RETRY_BACKOFF_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
                            Duration minBackoff = orElse(source.getDuration(RSOCKET_SERVER_RESUME_RETRY_BACKOFF_MIN_BACKOFF_KEY), DEFAULT_RETRY_MIN_BACKOFF);
                            configuration.resume.retry(backoff(maxAttempts, minBackoff));
                            break;
                        case FIXED_DELAY:
                            maxAttempts = orElse(source.getLong(RSOCKET_SERVER_RESUME_RETRY_FIXED_DELAY_MAX_ATTEMPTS_KEY), DEFAULT_RETRY_MAX_ATTEMPTS);
                            Duration fixedDelay = orElse(source.getDuration(RSOCKET_SERVER_RESUME_RETRY_FIXED_DELAY_KEY), DEFAULT_RETRY_FIXED_DELAY);
                            configuration.resume.retry(fixedDelay(maxAttempts, fixedDelay));
                            break;
                        case MAX:
                            int max = orElse(source.getInt(RSOCKET_SERVER_RESUME_RETRY_MAX_KEY), DEFAULT_RETRY_MAX);
                            configuration.resume.retry(max(max));
                            break;
                        case MAX_IN_A_ROW:
                            int maxInRow = orElse(source.getInt(RSOCKET_SERVER_RESUME_RETRY_MAX_IN_ROW_KEY), DEFAULT_RETRY_MAX_IN_ROW);
                            configuration.resume.retry(maxInARow(maxInRow));
                            break;
                        case INDEFINITELY:
                            break;
                    }
                }
            }

            configuration.payloadDecoder = rsocketPayloadDecoder(source.getString(RSOCKET_SERVER_PAYLOAD_DECODER_KEY)) == DEFAULT
                    ? PayloadDecoder.DEFAULT
                    : PayloadDecoder.ZERO_COPY;

            configuration.maxInboundPayloadSize = orElse(source.getInt(RSOCKET_SERVER_MAX_INBOUND_PAYLOAD_SIZE_KEY), 0);

            configuration.transport = rsocketTransport(source.getString(RSOCKET_SERVER_TRANSPORT_MODE_KEY));

            int port = orElse(source.getInt(RSOCKET_SERVER_TRANSPORT_PORT_KEY), DEFAULT_PORT);
            String host = orElse(source.getString(RSOCKET_SERVER_TRANSPORT_HOST_KEY), BROADCAST_IP_ADDRESS);

            switch (configuration.transport) {
                case TCP:
                    configuration.tcpServer = TcpServer.create().port(port).host(host);
                    break;
                case WEB_SOCKET:
                    configuration.httpWebSocketServer = HttpServer.create().port(port).host(host);
                    break;
            }

            return this;
        }
    }

}
