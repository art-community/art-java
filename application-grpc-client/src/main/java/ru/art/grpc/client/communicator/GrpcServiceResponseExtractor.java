package ru.art.grpc.client.communicator;

import ru.art.entity.Value;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.grpc.servlet.GrpcResponse;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;
import static java.util.Objects.nonNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.RESPONSE_OK;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;

public interface GrpcServiceResponseExtractor {
    static <ResponseType> ResponseType extractServiceResponse(GrpcCommunicationConfiguration configuration, GrpcResponse response) {
        String errorCode = response.getErrorCode();
        String errorMessage = response.getErrorMessage();
        Value responseDataValue = readProtobuf(response.getResponseData());
        ValueToModelMapper<?, ? extends Value> responseMapper = configuration.getResponseMapper();
        ServiceResponse.ServiceResponseBuilder<?> serviceResponseBuilder = ServiceResponse.builder().command(new ServiceMethodCommand(configuration.getServiceId(), configuration.getMethodId()));
        if (!RESPONSE_OK.equals(errorCode)) {
            serviceResponseBuilder.serviceException(new ServiceExecutionException(errorCode, errorMessage));
        }
        if (nonNull(responseDataValue) && nonNull(responseMapper)) {
            return cast(serviceResponseBuilder.responseData(cast(responseMapper.map(cast(responseDataValue)))).build());
        }
        return cast(serviceResponseBuilder.build());
    }
}
