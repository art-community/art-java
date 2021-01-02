package io.art.value.mapping;

import io.art.core.model.*;
import io.art.value.exception.*;
import io.art.value.immutable.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.value.constants.ValueConstants.ExceptionMessages.METHOD_ID_NOT_PRESENTED;
import static io.art.value.constants.ValueConstants.ExceptionMessages.SERVICE_ID_NOT_PRESENTED;
import static io.art.value.constants.ValueConstants.Keys.*;
import static io.art.value.factory.PrimitivesFactory.stringPrimitive;
import static io.art.value.immutable.Entity.entityBuilder;
import static io.art.value.mapping.PrimitiveMapping.toString;
import static java.text.MessageFormat.format;

@UtilityClass
public class ServiceIdMapping {
    public ServiceMethodIdentifier toServiceMethod(Entity entity) {
        String serviceId = entity.mapping().map(SERVICE_ID_KEY, toString);
        String methodId = entity.mapping().map(METHOD_ID_KEY, toString);
        if (isEmpty(serviceId)) {
            throw new ValueMappingException(format(SERVICE_ID_NOT_PRESENTED, entity));
        }
        if (isEmpty(methodId)) {
            throw new ValueMappingException(format(METHOD_ID_NOT_PRESENTED, entity));
        }
        return new ServiceMethodIdentifier(serviceId, methodId);
    }

    public Entity fromServiceMethod(ServiceMethodIdentifier id) {
        return entityBuilder()
                .put(SERVICE_METHOD_IDENTIFIERS_KEY, entityBuilder()
                        .put(stringPrimitive(SERVICE_ID_KEY), stringPrimitive(id.getServiceId()))
                        .put(stringPrimitive(METHOD_ID_KEY), stringPrimitive(id.getMethodId()))
                        .build())
                .build();
    }
}
