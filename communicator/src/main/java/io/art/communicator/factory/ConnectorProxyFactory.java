package io.art.communicator.factory;

import io.art.communicator.exception.*;
import io.art.communicator.proxy.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.communicator.constants.CommunicatorConstants.Errors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.constants.StringConstants.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class ConnectorProxyFactory {
    public static <T> T createConnectorProxy(MetaClass<T> connectorClass, Function<MetaMethod<?>, CommunicatorProxy<?>> provider) {
        Function<MetaClass<?>, Object> creator = proxy -> proxy.creator().instantiate();

        Map<MetaMethod<?>, CommunicatorProxy<?>> proxies = connectorClass.methods()
                .stream()
                .filter(method -> method.parameters().size() == 0)
                .filter(method -> nonNull(method.returnType().declaration()))
                .collect(mapCollector(identity(), provider));

        if (proxies.size() != connectorClass.methods().size()) {
            String invalidMethods = connectorClass.methods().stream()
                    .filter(method -> !proxies.containsKey(method))
                    .map(MetaMethod::toString)
                    .collect(joining(NEW_LINE));
            throw new CommunicatorException(format(CONNECTOR_HAS_INVALID_METHOD_FOR_PROXY, connectorClass.definition().type().getName(), invalidMethods));
        }

        Map<MetaMethod<?>, Function<Object, Object>> invocations = proxies
                .entrySet()
                .stream()
                .collect(mapCollector(Map.Entry::getKey, entry -> ignore -> creator.apply(entry.getKey().returnType().declaration())));

        MetaProxy proxy = connectorClass.proxy(invocations);

        if (isNull(proxy)) {
            throw new CommunicatorException(format(PROXY_IS_NULL, connectorClass.definition().type().getName()));
        }

        return cast(proxy);
    }
}
