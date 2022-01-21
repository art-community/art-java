package io.art.rsocket.interceptor;

import com.google.common.base.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.rsocket.*;
import io.rsocket.util.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.util.annotation.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Messages.*;
import static java.text.MessageFormat.*;
import static reactor.core.publisher.Flux.*;

public class RsocketLoggingProxy extends RSocketProxy {
    private final Logger logger;
    private final Property<Boolean> enabled;

    public RsocketLoggingProxy(Logger logger, RSocket rsocket, Property<Boolean> enabled) {
        super(rsocket);
        this.logger = logger;
        this.enabled = enabled;
    }

    @Override
    public Mono<Void> fireAndForget(@NonNull Payload payload) {
        if (!enabled.get()) return super.fireAndForget(payload);
        log(payload, FIRE_AND_FORGET_REQUEST_LOG);
        Mono<Void> output = super
                .fireAndForget(payload)
                .doOnError(error -> logger.error(format(FIRE_AND_FORGET_EXCEPTION_LOG, let(error, Throwables::getStackTraceAsString))));
        return output.doOnNext(nothing -> log(FIRE_AND_FORGET_RESPONSE_LOG));
    }

    @Override
    public Mono<Payload> requestResponse(@NonNull Payload payload) {
        if (!enabled.get()) return super.requestResponse(payload);
        log(payload, REQUEST_RESPONSE_REQUEST_LOG);
        Mono<Payload> output = super
                .requestResponse(payload)
                .doOnError(error -> logger.error(format(REQUEST_RESPONSE_EXCEPTION_LOG, let(error, Throwables::getStackTraceAsString))));
        return output.doOnNext(response -> log(response, RESPONSE_RESPONSE_LOG));
    }

    @Override
    public Flux<Payload> requestStream(@NonNull Payload payload) {
        if (!enabled.get()) return super.requestStream(payload);
        log(payload, REQUEST_STREAM_REQUEST_LOG);
        Flux<Payload> output = super
                .requestStream(payload)
                .doOnError(error -> logger.error(format(REQUEST_STREAM_EXCEPTION_LOG, let(error, Throwables::getStackTraceAsString))));
        return output.doOnNext(response -> log(response, REQUEST_STREAM_RESPONSE_LOG));
    }

    @Override
    public Flux<Payload> requestChannel(@NonNull Publisher<Payload> payloads) {
        if (!enabled.get()) return super.requestChannel(payloads);
        Flux<Payload> input = from(payloads)
                .doOnNext(payload -> log(payload, REQUEST_CHANNEL_REQUEST_LOG))
                .doOnError(error -> logger.error(format(REQUEST_CHANNEL_EXCEPTION_LOG, let(error, Throwables::getStackTraceAsString))));
        Flux<Payload> output = super.requestChannel(input);
        return output.doOnNext(payload -> log(payload, REQUEST_CHANNEL_RESPONSE_LOG));
    }

    @Override
    public Mono<Void> metadataPush(@NonNull Payload payload) {
        if (!enabled.get()) return super.metadataPush(payload);
        log(payload, METADATA_PUSH_REQUEST_LOG);
        Mono<Void> output = super
                .metadataPush(payload)
                .doOnError(error -> logger.error(format(METADATA_PUSH_EXCEPTION_LOG, let(error, Throwables::getStackTraceAsString))));
        return output.doOnNext(nothing -> log(METADATA_PUSH_RESPONSE_LOG));
    }

    private void log(String message) {
        if (!enabled.get()) return;
        logger.info(message);
    }

    private void log(Payload payload, String message) {
        if (!enabled.get()) return;
        logger.info(format(message, payload.getDataUtf8(), payload.getMetadataUtf8()));
    }
}
