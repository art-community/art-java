package io.art.rsocket.interceptor;

import io.rsocket.*;
import io.rsocket.util.*;
import org.apache.logging.log4j.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.util.annotation.*;
import static com.google.common.base.Throwables.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import static reactor.core.publisher.Flux.*;

public class RsocketLoggingProxy extends RSocketProxy {
    private final Logger logger;

    public RsocketLoggingProxy(Logger logger, RSocket rsocket) {
        super(rsocket);
        this.logger = logger;
    }

    @Override
    public Mono<Void> fireAndForget(@NonNull Payload payload) {
        logger.info(format(FIRE_AND_FORGET_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
        Mono<Void> output = super.fireAndForget(payload).doOnError(error -> logger.error(format(FIRE_AND_FORGET_EXCEPTION_LOG, getStackTraceAsString(error))));
        return output.doOnNext(nothing -> logger.info(FIRE_AND_FORGET_RESPONSE_LOG));
    }

    @Override
    public Mono<Payload> requestResponse(@NonNull Payload payload) {
        logger.info(format(REQUEST_RESPONSE_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
        Mono<Payload> output = super.requestResponse(payload).doOnError(error -> logger.error(format(REQUEST_RESPONSE_EXCEPTION_LOG, getStackTraceAsString(error))));
        return output.doOnNext(response -> logger.info(format(RESPONSE_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())));
    }

    @Override
    public Flux<Payload> requestStream(@NonNull Payload payload) {
        logger.info(format(REQUEST_STREAM_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
        Flux<Payload> output = super.requestStream(payload).doOnError(error -> logger.error(format(REQUEST_STREAM_EXCEPTION_LOG, getStackTraceAsString(error))));
        return output.doOnNext(response -> logger.info(format(REQUEST_STREAM_RESPONSE_LOG, response.getDataUtf8(), response.getMetadataUtf8())));
    }

    @Override
    public Flux<Payload> requestChannel(@NonNull Publisher<Payload> payloads) {
        Flux<Payload> input = from(payloads)
                .doOnNext(payload -> logger.info(format(REQUEST_CHANNEL_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8())))
                .doOnError(error -> logger.error(format(REQUEST_CHANNEL_EXCEPTION_LOG, getStackTraceAsString(error))));
        Flux<Payload> output = super.requestChannel(input);
        return output.doOnNext(payload -> logger.info(format(REQUEST_CHANNEL_RESPONSE_LOG, payload.getDataUtf8(), payload.getMetadataUtf8())));
    }

    @Override
    public Mono<Void> metadataPush(@NonNull Payload payload) {
        logger.info(format(METADATA_PUSH_REQUEST_LOG, payload.getDataUtf8(), payload.getMetadataUtf8()));
        Mono<Void> output = super.metadataPush(payload).doOnError(error -> logger.error(format(METADATA_PUSH_EXCEPTION_LOG, getStackTraceAsString(error))));
        return output.doOnNext(nothing -> logger.info(METADATA_PUSH_RESPONSE_LOG));
    }
}
