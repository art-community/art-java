package ru.adk.state.api.mapping;

import ru.adk.entity.mapper.ValueToModelMapper.StringParametersMapToModelMapper;
import ru.adk.state.api.model.ClusterProfileRequest;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.PathParameters.PROFILE;

public interface NetworkServicePathParametersMapper {
    StringParametersMapToModelMapper<ClusterProfileRequest> toClusterProfileRequest = value -> ClusterProfileRequest.builder().profile(value.getParameter(PROFILE)).build();
}
