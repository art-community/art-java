package ru.art.platform.api.mapping;

import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;

import java.lang.String;
import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.platform.api.model.User;

public interface UserMapper {
	String id = "id";

	String token = "token";

	String name = "name";

	String email = "email";

	ValueToModelMapper<User, Entity> toUser = entity -> isNotEmpty(entity) ? User.builder()
			.id(entity.getLong(id))
			.token(entity.getString(token))
			.name(entity.getString(name))
			.email(entity.getString(email))
			.build() : User.builder().build();

	ValueFromModelMapper<User, Entity> fromUser = model -> isNotEmpty(model) ? Entity.entityBuilder()
			.longField(id, model.getId())
			.stringField(token, model.getToken())
			.stringField(name, model.getName())
			.stringField(email, model.getEmail())
			.build() : Entity.entityBuilder().build();
}
