package io.art.rsocket.configuration.communicator.common;

import io.art.core.changes.*;
import io.art.core.source.*;
import io.art.core.strategy.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.art.transport.configuration.*;
import io.rsocket.core.*;
import io.rsocket.plugins.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.strategy.ServiceMethodStrategy.*;
import static io.art.rsocket.configuration.common.RsocketKeepAliveConfiguration.*;
import static io.art.rsocket.configuration.common.RsocketResumeConfiguration.*;
import static io.art.rsocket.configuration.common.RsocketSslConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import static io.art.transport.configuration.TransportRetryConfiguration.*;
import static java.util.function.UnaryOperator.*;
import java.time.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class RsocketCommonConnectorConfiguration {
    private String connector;
    private DataFormat dataFormat;
    private DataFormat metaDataFormat;
    private boolean verbose;
    private int fragment;
    private RsocketKeepAliveConfiguration keepAlive;
    private RsocketResumeConfiguration resume;
    private TransportRetryConfiguration retry;
    private PayloadDecoderMode payloadDecoderMode;
    private int maxInboundPayloadSize;
    private ServiceMethodStrategy service;
    private Duration timeout;
    private UnaryOperator<InterceptorRegistry> interceptors;
    private UnaryOperator<RSocketConnector> decorator;
    private RsocketSslConfiguration ssl;

    public static RsocketCommonConnectorConfiguration commonConnectorConfiguration(String connector) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        configuration.dataFormat = MESSAGE_PACK;
        configuration.metaDataFormat = MESSAGE_PACK;
        configuration.verbose = false;
        configuration.connector = connector;
        configuration.fragment = 0;
        configuration.payloadDecoderMode = ZERO_COPY;
        configuration.maxInboundPayloadSize = Integer.MAX_VALUE;
        configuration.service = byCommunicator();
        configuration.timeout = DEFAULT_TIMEOUT;
        configuration.interceptors = identity();
        configuration.decorator = identity();
        configuration.keepAlive = rsocketKeepAlive();
        configuration.resume = rsocketResume();
        configuration.retry = retry();
        return configuration;
    }

    public static RsocketCommonConnectorConfiguration commonConnectorConfiguration(RsocketModuleRefresher refresher, RsocketCommonConnectorConfiguration current, ConfigurationSource source) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        ChangesListener listener = refresher.connectorListeners().listenerFor(current.connector);
        ChangesListener loggingListener = refresher.connectorLoggingListeners().listenerFor(current.connector);
        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), current.dataFormat));
        configuration.metaDataFormat = listener.emit(dataFormat(source.getString(META_DATA_FORMAT_KEY), current.metaDataFormat));
        configuration.verbose = loggingListener.emit(orElse(source.getBoolean(VERBOSE_KEY), current.verbose));
        configuration.connector = current.connector;
        configuration.interceptors = current.interceptors;
        configuration.decorator = current.decorator;
        configuration.fragment = listener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), current.fragment));
        configuration.keepAlive = listener.emit(let(source.getNested(KEEP_ALIVE_SECTION), section -> rsocketKeepAlive(section, current.keepAlive), current.keepAlive));
        configuration.resume = listener.emit(let(source.getNested(RESUME_SECTION), section -> rsocketResume(section, current.resume), current.resume));
        configuration.retry = listener.emit(let(source.getNested(RECONNECT_SECTION), section -> retry(section, current.retry), current.retry));
        configuration.payloadDecoderMode = listener.emit(rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), current.payloadDecoderMode));
        configuration.maxInboundPayloadSize = listener.emit(orElse(source.getInteger(MAX_INBOUND_PAYLOAD_SIZE_KEY), current.maxInboundPayloadSize));
        configuration.service = listener.emit(let(source.getString(SERVICE_ID_KEY), ServiceMethodStrategy::manual, current.service));
        configuration.timeout = listener.emit(orElse(source.getDuration(TIMEOUT_KEY), current.timeout));
        configuration.ssl = listener.emit(orElse(source.getNested(SSL_SECTION, section -> rsocketSsl(section, current.ssl)), current.ssl));
        return configuration;
    }
}
