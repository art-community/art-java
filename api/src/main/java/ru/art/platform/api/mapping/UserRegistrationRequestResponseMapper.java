package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.UserRegistrationRequest;
import ru.art.platform.api.model.UserRegistrationResponse;

public interface UserRegistrationRequestResponseMapper {
	interface UserRegistrationRequestMapper {
		String name = "name";

		String password = "password";

		String email = "email";

		ValueToModelMapper<UserRegistrationRequest, Entity> toUserRegistrationRequest = entity -> isNotEmpty(entity) ? UserRegistrationRequest.builder()
				.name(entity.getString(name))
				.password(entity.getString(password))
				.email(entity.getString(email))
				.build() : UserRegistrationRequest.builder().build();

		ValueFromModelMapper<UserRegistrationRequest, Entity> fromUserRegistrationRequest = model -> isNotEmpty(model) ? Entity.entityBuilder()
				.stringField(name, model.getName())
				.stringField(password, model.getPassword())
				.stringField(email, model.getEmail())
				.build() : Entity.entityBuilder().build();
	}

	interface UserRegistrationResponseMapper {
		String user = "user";

		String token = "token";

		ValueToModelMapper<UserRegistrationResponse, Entity> toUserRegistrationResponse = entity -> isNotEmpty(entity) ? UserRegistrationResponse.builder()
				.user(entity.getValue(user, UserMapper.toUser))
				.token(entity.getString(token))
				.build() : UserRegistrationResponse.builder().build();

		ValueFromModelMapper<UserRegistrationResponse, Entity> fromUserRegistrationResponse = model -> isNotEmpty(model) ? Entity.entityBuilder()
				.entityField(user, model.getUser(), UserMapper.fromUser)
				.stringField(token, model.getToken())
				.build() : Entity.entityBuilder().build();
	}
}
