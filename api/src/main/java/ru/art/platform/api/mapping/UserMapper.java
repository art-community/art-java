package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.User;

public interface UserMapper {
	String id = "id";

	String name = "name";

	String password = "password";

	String email = "email";

	String token = "token";

	ValueToModelMapper<User, Entity> toUser = entity -> isNotEmpty(entity) ? User.builder()
			.id(entity.getLong(id))
			.name(entity.getString(name))
			.password(entity.getString(password))
			.email(entity.getString(email))
			.token(entity.getString(token))
			.build() : User.builder().build();

	ValueFromModelMapper<User, Entity> fromUser = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.longField(id, model.getId())
			.stringField(name, model.getName())
			.stringField(password, model.getPassword())
			.stringField(email, model.getEmail())
			.stringField(token, model.getToken())
			.build() : Entity.entityBuilder().build();
}
