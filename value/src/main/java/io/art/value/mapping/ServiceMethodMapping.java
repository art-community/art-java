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
    private final static Primitive SERVICE_ID = stringPrimitive(SERVICE_ID_KEY);
    private final static Primitive METHOD_ID = stringPrimitive(METHOD_ID_KEY);

    public ServiceMethodIdentifier toServiceMethod(Entity entity) {
        String serviceId = entity.map(SERVICE_ID, toString);
        String methodId = entity.map(METHOD_ID, toString);
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
                .put(SERVICE_ID, stringPrimitive(id.getServiceId()))
                .put(METHOD_ID, stringPrimitive(id.getMethodId()))
                .build();
    }
}
