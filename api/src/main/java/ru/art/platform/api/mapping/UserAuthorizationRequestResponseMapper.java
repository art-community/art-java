package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.UserAuthorizationRequest;
import ru.art.platform.api.model.UserAuthorizationResponse;

public interface UserAuthorizationRequestResponseMapper {
	interface UserAuthorizationRequestMapper {
		String name = "name";

		String password = "password";

		ValueToModelMapper<UserAuthorizationRequest, Entity> toUserAuthorizationRequest = entity -> isNotEmpty(entity) ? UserAuthorizationRequest.builder()
				.name(entity.getString(name))
				.password(entity.getString(password))
				.build() : UserAuthorizationRequest.builder().build();

		ValueFromModelMapper<UserAuthorizationRequest, Entity> fromUserAuthorizationRequest = model -> isNotEmpty(model) ? Entity.entityBuilder()
				.stringField(name, model.getName())
				.stringField(password, model.getPassword())
				.build() : Entity.entityBuilder().build();
	}

	interface UserAuthorizationResponseMapper {
		String user = "user";

		String token = "token";

		ValueToModelMapper<UserAuthorizationResponse, Entity> toUserAuthorizationResponse = entity -> isNotEmpty(entity) ? UserAuthorizationResponse.builder()
				.user(entity.getValue(user, UserMapper.toUser))
				.token(entity.getString(token))
				.build() : UserAuthorizationResponse.builder().build();

		ValueFromModelMapper<UserAuthorizationResponse, Entity> fromUserAuthorizationResponse = model -> isNotEmpty(model) ? Entity.entityBuilder()
				.entityField(user, model.getUser(), UserMapper.fromUser)
				.stringField(token, model.getToken())
				.build() : Entity.entityBuilder().build();
	}
}
