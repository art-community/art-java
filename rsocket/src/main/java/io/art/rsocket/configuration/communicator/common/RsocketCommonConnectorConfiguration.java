package io.art.rsocket.configuration.communicator.common;

import io.art.core.changes.*;
import io.art.core.model.*;
import io.art.core.source.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.art.transport.payload.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.configuration.common.RsocketKeepAliveConfiguration.*;
import static io.art.rsocket.configuration.common.RsocketResumeConfiguration.*;
import static io.art.rsocket.configuration.common.RsocketRetryConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import static io.art.transport.constants.TransportModuleConstants.DataFormat.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class RsocketCommonConnectorConfiguration {
    private String connector;
    private DataFormat dataFormat;
    private DataFormat metaDataFormat;
    private boolean logging;
    private int fragment;
    private RsocketKeepAliveConfiguration keepAlive;
    private RsocketResumeConfiguration resume;
    private RsocketRetryConfiguration retry;
    private PayloadDecoderMode payloadDecoderMode;
    private int maxInboundPayloadSize;
    private String targetService;
    private Supplier<TransportPayloadWriter> setupPayloadWriter;

    public static RsocketCommonConnectorConfiguration defaults(String connector) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        configuration.dataFormat = JSON;
        configuration.metaDataFormat = JSON;
        configuration.logging = false;
        configuration.connector = connector;
        configuration.fragment = 0;
        configuration.payloadDecoderMode = ZERO_COPY;
        configuration.maxInboundPayloadSize = Integer.MAX_VALUE;
        configuration.setupPayloadWriter = () -> new TransportPayloadWriter(configuration.dataFormat);
        return configuration;
    }

    public static RsocketCommonConnectorConfiguration from(RsocketModuleRefresher refresher, ConfigurationSource source) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        ChangesListener listener = refresher.connectorListeners().listenerFor(source.getSection());
        ChangesListener loggingListener = refresher.connectorLoggingListeners().listenerFor(configuration.connector);
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), false));
        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), JSON));
        configuration.metaDataFormat = listener.emit(dataFormat(source.getString(META_DATA_FORMAT_KEY), JSON));
        configuration.connector = source.getSection();
        configuration.fragment = listener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), 0));
        configuration.keepAlive = listener.emit(source.getNested(KEEP_ALIVE_SECTION, RsocketKeepAliveConfiguration::rsocketKeepAlive));
        configuration.resume = listener.emit(source.getNested(KEEP_ALIVE_SECTION, RsocketResumeConfiguration::rsocketResume));
        configuration.retry = listener.emit(source.getNested(KEEP_ALIVE_SECTION, RsocketRetryConfiguration::rsocketRetry));
        configuration.payloadDecoderMode = listener.emit(rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), ZERO_COPY));
        configuration.maxInboundPayloadSize = listener.emit(orElse(source.getInteger(MAX_INBOUND_PAYLOAD_SIZE_KEY), Integer.MAX_VALUE));
        return configuration;
    }

    public static RsocketCommonConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketCommonConnectorConfiguration current, ConfigurationSource source) {
        RsocketCommonConnectorConfiguration configuration = RsocketCommonConnectorConfiguration.builder().build();
        ChangesListener listener = refresher.connectorListeners().listenerFor(current.connector);
        ChangesListener loggingListener = refresher.connectorLoggingListeners().listenerFor(configuration.connector);
        configuration.dataFormat = listener.emit(dataFormat(source.getString(DATA_FORMAT_KEY), current.dataFormat));
        configuration.metaDataFormat = listener.emit(dataFormat(source.getString(META_DATA_FORMAT_KEY), current.metaDataFormat));
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), current.logging));
        configuration.connector = current.connector;
        configuration.fragment = listener.emit(orElse(source.getInteger(FRAGMENTATION_MTU_KEY), current.fragment));
        configuration.keepAlive = listener.emit(let(source.getNested(KEEP_ALIVE_SECTION), section -> rsocketKeepAlive(section, current.keepAlive), current.keepAlive));
        configuration.resume = listener.emit(let(source.getNested(RESUME_SECTION), section -> rsocketResume(section, current.resume), current.resume));
        configuration.retry = listener.emit(let(source.getNested(RECONNECT_SECTION), section -> rsocketRetry(section, current.retry), current.retry));
        configuration.payloadDecoderMode = listener.emit(rsocketPayloadDecoder(source.getString(PAYLOAD_DECODER_KEY), current.payloadDecoderMode));
        configuration.maxInboundPayloadSize = listener.emit(orElse(source.getInteger(MAX_INBOUND_PAYLOAD_SIZE_KEY), current.maxInboundPayloadSize));
        return configuration;
    }
}
