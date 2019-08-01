package ru.adk.configurator.api.mapping;

import ru.adk.configurator.api.entity.UserRequest;
import ru.adk.configurator.api.entity.UserResponse;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.adk.entity.Entity.entityBuilder;

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
