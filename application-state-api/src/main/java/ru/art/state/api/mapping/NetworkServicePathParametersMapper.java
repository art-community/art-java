package ru.art.state.api.mapping;

import ru.art.entity.mapper.ValueToModelMapper.StringParametersMapToModelMapper;
import ru.art.state.api.model.ClusterProfileRequest;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.PathParameters.PROFILE;

public interface NetworkServicePathParametersMapper {
    StringParametersMapToModelMapper<ClusterProfileRequest> toClusterProfileRequest = value -> ClusterProfileRequest.builder().profile(value.getParameter(PROFILE)).build();
}
