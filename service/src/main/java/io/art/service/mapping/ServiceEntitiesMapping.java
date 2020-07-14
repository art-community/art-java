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

package io.art.service.mapping;

import io.art.entity.*;
import io.art.entity.mapper.*;
import io.art.service.exception.*;
import static io.art.entity.mapper.ValueMapper.*;
import static io.art.service.mapping.ServiceEntitiesMapping.ServiceExecutionExceptionMapping.ServiceExecutionExceptionFields.*;
import static io.art.service.model.ServiceMethodCommand.*;

public interface ServiceEntitiesMapping {

    interface ServiceExecutionExceptionMapping {
        ValueToModelMapper<ServiceExecutionException, Entity> toServiceExecutionException = (entity) -> new ServiceExecutionException(
                parseServiceCommand(entity.getString(SERVICE_COMMAND)),
                entity.getString(ERROR_CODE),
                entity.getString(ERROR_MESSAGE)
        );
        ValueFromModelMapper<ServiceExecutionException, Entity> fromServiceExecutionException = (exception) -> Entity.entityBuilder()
                .stringField(ERROR_CODE, exception.getErrorCode())
                .stringField(ERROR_MESSAGE, exception.getMessage())
                .build();
        ValueMapper<ServiceExecutionException, Entity> serviceExecutionExceptionMapper = mapper(fromServiceExecutionException, toServiceExecutionException);

        interface ServiceExecutionExceptionFields {
            String SERVICE_COMMAND = "serviceCommand";
            String ERROR_CODE = "errorCode";
            String ERROR_MESSAGE = "errorMessage";
        }
    }
}
