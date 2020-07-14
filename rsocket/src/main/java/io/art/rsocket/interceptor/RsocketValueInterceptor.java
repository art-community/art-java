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

package io.art.rsocket.interceptor;

import io.rsocket.*;
import io.rsocket.plugins.*;
import io.rsocket.util.*;
import lombok.*;
import lombok.experimental.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import io.art.entity.Value;
import io.art.entity.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.reader.*;
import io.art.rsocket.state.RsocketModuleState.*;
import io.art.service.exception.*;
import io.art.service.model.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Mono.*;
import static io.art.entity.Value.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketInterceptingDataType.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.writer.RsocketPayloadWriter.*;
import static io.art.service.constants.ServiceExceptionsMessages.*;
import static io.art.service.mapping.ServiceRequestMapping.REQUEST_DATA;
import static io.art.service.mapping.ServiceRequestMapping.*;
import javax.annotation.*;
import java.util.function.*;

@Getter
@Setter
@Accessors(fluent = true)
public class RsocketValueInterceptor implements RSocketInterceptor {
    private final RsocketInterceptingDataType dataType = META_DATA;
    private BiFunction<Payload, RsocketDataFormat, Value> valueByType;

    private BiFunction<RsocketPayloadValue, RSocket, RsocketInterceptedResultAction> onFireAndForget;
    private BiFunction<RsocketPayloadValue, RSocket, RsocketValueInterceptionResult> onRequestResponse;
    private BiFunction<RsocketPayloadValue, RSocket, RsocketValueInterceptionResult> onRequestStream;
    private BiFunction<RsocketPayloadValue, RSocket, RsocketValueInterceptionResult> onRequestChannel;
    private BiFunction<RsocketPayloadValue, RSocket, RsocketInterceptedResultAction> onMetadataPush;

    private Predicate<ServiceMethodCommand> predicate = request -> true;

    public RsocketValueInterceptor(RsocketInterceptingDataType dataType) {
        switch (dataType) {
            case DATA:
                valueByType = RsocketPayloadReader::readPayloadData;
                break;
            case META_DATA:
                valueByType = RsocketPayloadReader::readPayloadMetaData;
                break;
            default:
                throw new RsocketServerException(INTERCEPTING_DATA_TYPE_NULL);
        }
    }

    @Override
    public RSocket apply(RSocket rsocket) {
        return new RSocketProxy(rsocket) {

            @Override
            public Mono<Void> fireAndForget(@Nonnull Payload payload) {
                if (isNull(onFireAndForget)) {
                    return super.fireAndForget(payload);
                }
                CurrentRsocketState state = rsocketModuleState().currentRocketState();
                Value data = valueByType.apply(payload, state.getDataFormat());
                if (!isEntity(data)) {
                    return super.fireAndForget(payload);
                }
                if (testServiceMethodCommand(data)) {
                    RsocketInterceptedResultAction action = onMetadataPush.apply(new RsocketPayloadValue(payload, asEntity(data).getValue(REQUEST_DATA)), rsocket);
                    switch (action) {
                        case RETURN:
                            return never();
                        case PROCESS:
                            return super.metadataPush(payload);
                    }
                }
                return super.fireAndForget(payload);
            }

            @Override
            public Mono<Payload> requestResponse(@Nonnull Payload payload) {
                if (isNull(onRequestResponse)) {
                    return super.requestResponse(payload);
                }
                CurrentRsocketState state = rsocketModuleState().currentRocketState();
                Value data = valueByType.apply(payload, state.getDataFormat());
                if (!isEntity(data)) {
                    return super.requestResponse(payload);
                }
                if (testServiceMethodCommand(data)) {
                    RsocketValueInterceptionResult result = onRequestResponse.apply(new RsocketPayloadValue(payload, asEntity(data).getValue(REQUEST_DATA)), rsocket);
                    switch (result.getAction()) {
                        case RETURN:
                            return Mono.just(writePayloadData(result.getValue(), state.getDataFormat()));
                        case PROCESS:
                            return super.requestResponse(payload);
                    }
                }
                return super.requestResponse(payload);
            }

            @Override
            public Flux<Payload> requestStream(@Nonnull Payload payload) {
                if (isNull(onRequestStream)) {
                    return super.requestStream(payload);
                }
                CurrentRsocketState state = rsocketModuleState().currentRocketState();
                Value data = valueByType.apply(payload, state.getDataFormat());
                if (!isEntity(data)) {
                    return super.requestStream(payload);
                }

                if (testServiceMethodCommand(data)) {
                    RsocketValueInterceptionResult result = onRequestStream.apply(new RsocketPayloadValue(payload, asEntity(data).getValue(REQUEST_DATA)), rsocket);
                    switch (result.getAction()) {
                        case RETURN:
                            return Flux.just(writePayloadData(result.getValue(), state.getDataFormat()));
                        case PROCESS:
                            return super.requestStream(payload);
                    }
                }

                return super.requestStream(payload);
            }

            @Override
            public Flux<Payload> requestChannel(@Nonnull Publisher<Payload> payloads) {
                if (isNull(onRequestChannel)) {
                    return super.requestChannel(payloads);
                }
                CurrentRsocketState state = rsocketModuleState().currentRocketState();

                EmitterProcessor<Payload> responseProcessor = EmitterProcessor.create();
                FluxSink<Payload> mainResponseEmitter = responseProcessor.sink();
                FluxSink<Payload> interceptedResponseEmitter = responseProcessor.sink();

                EmitterProcessor<Payload> interceptingRequestProcessor = EmitterProcessor.create();
                FluxSink<Payload> interceptingRequestEmitter = interceptingRequestProcessor.sink();

                EmitterProcessor<Payload> mainRequestProcessor = EmitterProcessor.create();
                FluxSink<Payload> mainRequestEmitter = mainRequestProcessor.sink();

                Subscriber<Payload> mainResponseSubscriber = createSubscriber(mainResponseEmitter);

                Subscriber<Payload> interceptedResponseSubscriber = createSubscriber(interceptedResponseEmitter);

                Subscriber<Payload> interceptingRequestSubscriber = new Subscriber<Payload>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        interceptingRequestEmitter.onCancel(subscription::cancel);
                        interceptingRequestEmitter.onRequest(subscription::request);
                        mainRequestEmitter.onCancel(subscription::cancel);
                        mainRequestEmitter.onRequest(subscription::request);
                    }

                    @Override
                    public void onNext(Payload payload) {
                        System.out.println(RsocketPayloadReader.readPayloadData(payload, state.getDataFormat()));
                        Value data = valueByType.apply(payload, state.getDataFormat());
                        if (!isEntity(data)) {
                            mainRequestEmitter.next(payload);
                            return;
                        }

                        if (testServiceMethodCommand(data)) {
                            RsocketValueInterceptionResult result = onRequestChannel.apply(new RsocketPayloadValue(payload, asEntity(data).getValue(REQUEST_DATA)), rsocket);
                            switch (result.getAction()) {
                                case RETURN:
                                    interceptedResponseEmitter.next(writePayloadData(result.getValue(), state.getDataFormat()));
                                    return;
                                case PROCESS:
                                    mainRequestEmitter.next(payload);
                                    return;
                            }
                        }
                        mainRequestEmitter.next(payload);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        interceptingRequestEmitter.error(throwable);
                        mainRequestEmitter.error(throwable);
                    }

                    @Override
                    public void onComplete() {
                        interceptingRequestEmitter.complete();
                        mainRequestEmitter.complete();
                    }
                };

                payloads.subscribe(interceptingRequestSubscriber);

                interceptingRequestProcessor.subscribe(interceptedResponseSubscriber);
                super.requestChannel(mainRequestProcessor).subscribe(mainResponseSubscriber);

                return responseProcessor;
            }

            @Override
            public Mono<Void> metadataPush(@Nonnull Payload payload) {
                if (isNull(onMetadataPush)) {
                    return super.metadataPush(payload);
                }
                CurrentRsocketState state = rsocketModuleState().currentRocketState();
                Value data = valueByType.apply(payload, state.getDataFormat());
                if (!isEntity(data)) {
                    return super.metadataPush(payload);
                }
                if (testServiceMethodCommand(data)) {
                    RsocketInterceptedResultAction action = onMetadataPush.apply(new RsocketPayloadValue(payload, asEntity(data).getValue(REQUEST_DATA)), rsocket);
                    switch (action) {
                        case RETURN:
                            return never();
                        case PROCESS:
                            return super.metadataPush(payload);
                    }
                }
                return super.metadataPush(payload);
            }
        };
    }

    private boolean testServiceMethodCommand(Value data) {
        Entity serviceMethodCommandEntity = Value.asEntity(data).getEntity(SERVICE_METHOD_COMMAND);
        if (isNull(serviceMethodCommandEntity)) throw new ServiceMappingException(SERVICE_COMMAND_IS_NULL);
        String serviceId = serviceMethodCommandEntity.getString(SERVICE_ID);
        if (isNull(serviceId)) throw new ServiceMappingException(SERVICE_ID_IS_NULL);
        String methodId = serviceMethodCommandEntity.getString(METHOD_ID);
        if (isNull(methodId)) throw new ServiceMappingException(METHOD_ID_IS_NULL);
        return predicate.test(new ServiceMethodCommand(serviceId, methodId));
    }

    private Subscriber<Payload> createSubscriber(FluxSink<Payload> emitter) {
        return new Subscriber<Payload>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                emitter.onRequest(subscription::request);
                emitter.onCancel(subscription::cancel);
            }

            @Override
            public void onNext(Payload payload) {
                emitter.next(payload);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.error(throwable);
            }

            @Override
            public void onComplete() {
                emitter.complete();
            }
        };
    }

    public static RsocketValueInterceptor rsocketDataInterceptor() {
        return new RsocketValueInterceptor(DATA);
    }

    public static RsocketValueInterceptor rsocketMetaDataInterceptor() {
        return new RsocketValueInterceptor(META_DATA);
    }
}
