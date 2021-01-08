package io.art.value.mapping;

import io.art.core.model.*;
import io.art.value.exception.*;
import io.art.value.immutable.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.value.constants.ValueModuleConstants.ExceptionMessages.*;
import static io.art.value.constants.ValueModuleConstants.Fields.*;
import static io.art.value.factory.PrimitivesFactory.*;
import static io.art.value.immutable.Entity.*;
import static io.art.value.mapping.PrimitiveMapping.*;
import static java.text.MessageFormat.*;

@UtilityClass
public class ServiceMethodMapping {
    public ServiceMethodIdentifier toServiceMethod(Entity entity) {
        String serviceId = entity.mapping().map(SERVICE_ID_KEY, toString);
        String methodId = entity.mapping().map(METHOD_ID_KEY, toString);
        if (isEmpty(serviceId)) {
            throw new ValueMappingException(format(SERVICE_ID_NOT_PRESENTED, entity.toMap()));
        }
        if (isEmpty(methodId)) {
            throw new ValueMappingException(format(METHOD_ID_NOT_PRESENTED, entity.toMap()));
        }
        return new ServiceMethodIdentifier(serviceId, methodId);
    }

    public Entity fromServiceMethod(ServiceMethodIdentifier id) {
        return entityBuilder()
                .put(stringPrimitive(SERVICE_ID_KEY), stringPrimitive(id.getServiceId()))
                .put(stringPrimitive(METHOD_ID_KEY), stringPrimitive(id.getMethodId()))
                .build();
    }
}
