package ru.adk.grpc.client.communicator;

import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.grpc.servlet.GrpcResponse;
import ru.adk.service.exception.ServiceExecutionException;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceResponse;
import static java.util.Objects.nonNull;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.grpc.client.constants.GrpcClientModuleConstants.RESPONSE_OK;
import static ru.adk.protobuf.descriptor.ProtobufEntityReader.readProtobuf;

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
