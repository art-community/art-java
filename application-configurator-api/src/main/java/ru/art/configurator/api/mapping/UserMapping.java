package ru.art.configurator.api.mapping;

import ru.art.configurator.api.entity.UserRequest;
import ru.art.configurator.api.entity.UserResponse;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.entity.Entity.entityBuilder;

public interface UserMapping {
    EntityToModelMapper<UserRequest> userRequestToModelMapper = value -> new UserRequest(value.getString("username"), value.getString("password"));
    EntityFromModelMapper<UserRequest> userRequestFromModelMapper = userRequest -> entityBuilder()
            .stringField("username", userRequest.getName())
            .stringField("password", userRequest.getPassword())
            .build();

    EntityToModelMapper<UserResponse> userResponseToModelMapper = value -> new UserResponse(value.getBool("success"), value.getString("token"));
    EntityFromModelMapper<UserResponse> userResponseFromModelMapper = userRequest -> entityBuilder()
            .boolField("success", userRequest.isSuccess())
            .stringField("token", userRequest.getToken())
            .build();
}
