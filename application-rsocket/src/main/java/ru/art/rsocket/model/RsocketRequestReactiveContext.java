package ru.art.rsocket.model;

import io.rsocket.Payload;
import lombok.Builder;
import lombok.Getter;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.service.model.ServiceMethodCommand;
import static java.util.Objects.isNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.entity.Value.asEntity;
import static ru.art.rsocket.constants.RsocketModuleConstants.REQUEST_DATA;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.extractor.EntityServiceCommandExtractor.extractServiceMethodCommand;
import static ru.art.rsocket.model.RsocketReactiveMethods.fromCommand;
import static ru.art.rsocket.reader.RsocketPayloadReader.readPayload;

@Getter
@Builder
public class RsocketRequestReactiveContext {
    private final Object requestData;
    private final RsocketReactiveGroupKey rsocketReactiveGroupKey;
    private final RsocketReactiveMethods rsocketReactiveMethods;

    @SuppressWarnings("Duplicates")
    public static RsocketRequestReactiveContext fromPayload(Payload payload, RsocketDataFormat dataFormat) {
        Entity serviceRequestEntity = asEntity(readPayload(payload, dataFormat));
        ServiceMethodCommand command = extractServiceMethodCommand(serviceRequestEntity);
        RsocketReactiveMethods rsocketServiceMethods = fromCommand(command);
        ValueToModelMapper<?, ?> requestMapper;
        Value requestDataValue;
        if (isNull(requestMapper = rsocketServiceMethods.getRsocketMethod().requestMapper()) || isEmpty(requestDataValue = serviceRequestEntity.getValue(REQUEST_DATA))) {
            return RsocketRequestReactiveContext.builder()
                    .rsocketReactiveGroupKey(RsocketReactiveGroupKey.builder()
                            .serviceMethodCommand(command)
                            .responseMapper(rsocketServiceMethods.getRsocketMethod().responseMapper())
                            .validationPolicy(rsocketServiceMethods.getRsocketMethod().validationPolicy())
                            .build())
                    .rsocketReactiveMethods(rsocketServiceMethods)
                    .build();
        }
        return RsocketRequestReactiveContext.builder()
                .requestData(requestMapper.map(cast(requestDataValue)))
                .rsocketReactiveGroupKey(RsocketReactiveGroupKey.builder()
                        .serviceMethodCommand(command)
                        .responseMapper(rsocketServiceMethods.getRsocketMethod().responseMapper())
                        .validationPolicy(rsocketServiceMethods.getRsocketMethod().validationPolicy())
                        .build())
                .rsocketReactiveMethods(rsocketServiceMethods)
                .build();
    }
}
