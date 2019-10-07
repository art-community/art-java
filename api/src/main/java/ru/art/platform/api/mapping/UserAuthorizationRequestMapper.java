package ru.art.platform.api.mapping;

import ru.art.entity.*;
import ru.art.entity.mapper.*;
import ru.art.platform.api.model.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface UserAuthorizationRequestMapper {
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
