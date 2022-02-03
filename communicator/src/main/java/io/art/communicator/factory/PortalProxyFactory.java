package io.art.communicator.factory;

import io.art.communicator.*;
import io.art.communicator.exception.*;
import io.art.core.property.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.communicator.constants.CommunicatorConstants.Errors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static io.art.meta.extensions.MetaClassExtensions.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class PortalProxyFactory {
    public static <T extends Portal> T createPortalProxy(MetaClass<T> portalClass, Function<Class<? extends Communicator>, ? extends Communicator> provider) {
        Map<MetaMethod<?>, MetaClass<?>> proxies = portalClass.methods()
                .stream()
                .filter(method -> method.parameters().size() == 0)
                .filter(method -> nonNull(method.returnType().declaration()) && method.returnType().modifiers().contains(COMMUNICATOR))
                .collect(mapCollector(identity(), method -> method.returnType().declaration()));

        if (proxies.size() != portalClass.methods().size()) {
            String invalidMethods = joinMethods(portalClass, method -> !proxies.containsKey(method));
            throw new CommunicatorException(format(PORTAL_HAS_INVALID_METHODS, portalClass.definition().type(), invalidMethods));
        }

        LazyProperty<Map<String, ? extends Communicator>> cache = lazy(() -> proxies
                .entrySet()
                .stream()
                .collect(mapCollector(entry -> entry.getKey().name(), entry -> provider.apply(cast(entry.getKey().returnType().type())))));

        Map<MetaMethod<?>, Function<Object, ? extends Communicator>> invocations = proxies
                .entrySet()
                .stream()
                .collect(mapCollector(Map.Entry::getKey, entry -> ignore -> cache.get().get(entry.getKey().name())));

        return cast(portalClass.proxy(cast(invocations)));
    }
}
