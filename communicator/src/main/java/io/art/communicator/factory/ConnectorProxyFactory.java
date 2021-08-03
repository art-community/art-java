package io.art.communicator.factory;

import io.art.communicator.exception.*;
import io.art.communicator.model.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.communicator.constants.CommunicatorConstants.Errors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.LazyProperty.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class ConnectorProxyFactory {
    public static <T extends Connector> T createConnectorProxy(MetaClass<T> connectorClass, Function<Class<? extends Communicator>, ? extends Communicator> provider) {
        Map<MetaMethod<?>, MetaClass<?>> proxies = connectorClass.methods()
                .stream()
                .filter(method -> method.parameters().size() == 0)
                .filter(method -> nonNull(method.returnType().declaration()) && Communicator.class.isAssignableFrom(method.returnType().type()))
                .collect(mapCollector(identity(), method -> method.returnType().declaration()));

        if (proxies.size() != connectorClass.methods().size()) {
            String invalidMethods = connectorClass.methods().stream()
                    .filter(method -> !proxies.containsKey(method))
                    .map(MetaMethod::toString)
                    .collect(joining(NEW_LINE + NEW_LINE));
            throw new CommunicatorException(format(CONNECTOR_HAS_INVALID_METHOD_FOR_PROXY, connectorClass.definition().type().getName(), invalidMethods));
        }

        LazyProperty<Map<String, ? extends Communicator>> cache = lazy(() -> proxies
                .entrySet()
                .stream()
                .collect(mapCollector(entry -> entry.getKey().name(), entry -> provider.apply(cast(entry.getKey().returnType().type())))));

        Map<MetaMethod<?>, Function<Object, ? extends Communicator>> invocations = proxies
                .entrySet()
                .stream()
                .collect(mapCollector(Map.Entry::getKey, entry -> ignore -> cache.get().get(entry.getKey().name())));

        MetaProxy proxy = connectorClass.proxy(cast(invocations));

        if (isNull(proxy)) {
            throw new CommunicatorException(format(PROXY_IS_NULL, connectorClass.definition().type().getName()));
        }

        return cast(proxy);
    }
}
