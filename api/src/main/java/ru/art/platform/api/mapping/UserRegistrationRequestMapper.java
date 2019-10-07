package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.UserRegistrationRequest;

public interface UserRegistrationRequestMapper {
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
