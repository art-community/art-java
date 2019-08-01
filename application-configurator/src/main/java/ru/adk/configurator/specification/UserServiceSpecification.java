package ru.adk.configurator.specification;

import lombok.Getter;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.Methods.CHECK_TOKEN;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.Methods.LOGIN;
import static ru.adk.configurator.api.constants.ConfiguratorServiceConstants.USER_SERVICE_ID;
import static ru.adk.configurator.api.mapping.UserMapping.userRequestToModelMapper;
import static ru.adk.configurator.api.mapping.UserMapping.userResponseFromModelMapper;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.adk.configurator.service.UserService.checkToken;
import static ru.adk.configurator.service.UserService.login;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.entity.PrimitiveMapping.boolMapper;
import static ru.adk.entity.PrimitiveMapping.stringMapper;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.adk.http.server.model.HttpService.httpService;

@Getter
public class UserServiceSpecification implements HttpServiceSpecification {
    private final String serviceId = USER_SERVICE_ID;

    private final HttpService httpService = httpService()

            .post(LOGIN)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .requestMapper(userRequestToModelMapper)
            .produces(applicationJsonUtf8())
            .responseMapper(userResponseFromModelMapper)
            .listen(LOGIN_PATH)

            .post(CHECK_TOKEN)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .requestMapper(stringMapper.getToModel())
            .produces(applicationJsonUtf8())
            .responseMapper(boolMapper.getFromModel())
            .listen(CHECK_TOKEN_PATH)

            .serve(HTTP_PATH);

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case LOGIN:
                return cast(login(cast(request)));
            case CHECK_TOKEN:
                return cast(checkToken(cast(request)));
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
