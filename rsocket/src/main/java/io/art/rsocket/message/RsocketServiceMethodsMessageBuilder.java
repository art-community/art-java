package io.art.rsocket.message;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.server.method.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Messages.*;
import static java.text.MessageFormat.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@UtilityClass
public class RsocketServiceMethodsMessageBuilder {
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class ServiceKey {
        final MetaType<?> type;
        final String id;
    }

    public static String buildServiceMethodsMessage(ImmutableMap<ServiceMethodIdentifier, ServiceMethod> methods) {
        Map<ServiceKey, List<ServiceMethod>> serviceMethods = methods
                .values()
                .stream()
                .collect(groupingBy(entry -> new ServiceKey(entry.getInvoker().getOwner().definition(), entry.getId().getServiceId())));

        String methodsAsString = serviceMethods
                .entrySet()
                .stream()
                .map(entry -> format(RSOCKET_SERVICE_MESSAGE_PART, entry.getKey().id, entry.getKey().type)
                        + newLineTabulation(3)
                        + entry.getValue()
                        .stream()
                        .map(method -> format(RSOCKET_SERVICE_METHOD_MESSAGE_PART, method.getId().getMethodId(), method.getInvoker().getDelegate()))
                        .collect(joining(newLineTabulation(3))))
                .collect(joining(newLineTabulation(2)));

        return format(RSOCKET_SERVICES_MESSAGE_PART, methodsAsString);
    }
}
