/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.configurator.specification;

import lombok.*;
import ru.art.http.server.model.*;
import ru.art.http.server.specification.*;
import ru.art.service.exception.*;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.*;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.Methods.*;
import static ru.art.configurator.api.mapping.UserMapping.*;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.art.configurator.service.UserService.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.entity.PrimitiveMapping.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.model.HttpService.*;

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

            .serve(CONFIGURATOR_PATH);

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
