package ru.adk.rsocket.writer;

import io.rsocket.Payload;
import reactor.core.publisher.Flux;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.model.ServiceResponse;
import static io.rsocket.util.ByteBufPayload.create;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Flux.*;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.ArrayConstants.EMPTY_BYTES;
import static ru.adk.rsocket.constants.RsocketModuleConstants.REACTIVE_SERVICE_EXCEPTION_ERROR_CODE;
import static ru.adk.rsocket.writer.RsocketPayloadWriter.writePayload;
import static ru.adk.service.mapping.ServiceResponseMapping.fromServiceResponse;

public interface ServiceResponsePayloadWriter {
    static Payload writeServiceResponse(ValueFromModelMapper<?, ?> responseMapper, ServiceResponse<?> serviceResponse, RsocketDataFormat dataFormat) {
        return isNull(responseMapper) ?
                create(EMPTY_BYTES) :
                create(writePayload(fromServiceResponse(responseMapper).map(cast(serviceResponse)), dataFormat));
    }

    static Flux<Payload> writeResponseReactive(ValueFromModelMapper<?, ?> responseMapper, ServiceResponse<?> serviceResponse, RsocketDataFormat dataFormat) {
        return isNull(serviceResponse) || isNull(responseMapper) || isNull(serviceResponse.getResponseData()) ?
                never() :
                from(cast(serviceResponse.getResponseData()))
                        .map(response -> writePayload(fromServiceResponse(responseMapper).map(cast(ServiceResponse.builder()
                                .command(serviceResponse.getCommand())
                                .responseData(response)
                                .build())), dataFormat))
                        .onErrorResume(error -> just(writePayload(fromServiceResponse(responseMapper).map(cast(ServiceResponse.builder()
                                .command(serviceResponse.getCommand())
                                .serviceException(new ServiceExecutionException(serviceResponse.getCommand(), REACTIVE_SERVICE_EXCEPTION_ERROR_CODE, error))
                                .build())), dataFormat)));
    }
}
