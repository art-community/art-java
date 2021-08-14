package io.art.communicator.factory;

import io.art.communicator.action.*;
import io.art.communicator.exception.*;
import io.art.communicator.model.*;
import io.art.core.communication.*;
import io.art.meta.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.communicator.constants.CommunicatorConstants.Errors.*;
import static io.art.communicator.extensions.MetaClassExtensions.*;
import static io.art.communicator.factory.CommunicatorActionFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static java.text.MessageFormat.*;
import static java.util.function.Function.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class CommunicatorProxyFactory {
    public static <T extends Communicator> CommunicatorProxy<T> communicatorProxy(MetaClass<T> proxyClass, Supplier<Communication> communication) {
        return createCommunicatorProxy(proxyClass, method -> apply(communicatorAction(proxyClass, method, communication.get()), CommunicatorAction::initialize));
    }

    public static <T extends Communicator> CommunicatorProxy<T> preconfiguredCommunicatorProxy(MetaClass<T> proxyClass, Supplier<Communication> communication) {
        return createCommunicatorProxy(proxyClass, method -> apply(preconfiguredCommunicatorAction(proxyClass, method, communication.get()), CommunicatorAction::initialize));
    }

    public static <T extends Communicator> CommunicatorProxy<T> communicatorProxy(Class<T> proxyClass, Supplier<Communication> communication) {
        return communicatorProxy(Meta.declaration(proxyClass), communication);
    }

    public static <T extends Communicator> CommunicatorProxy<T> preconfiguredCommunicatorProxy(Class<T> proxyClass, Supplier<Communication> communication) {
        return preconfiguredCommunicatorProxy(Meta.declaration(proxyClass), communication);
    }

    public static <T extends Communicator> CommunicatorProxy<T> createCommunicatorProxy(MetaClass<T> proxyClass, Function<MetaMethod<?>, CommunicatorAction> provider) {
        Function<CommunicatorAction, Object> noArguments = CommunicatorAction::communicate;
        BiFunction<CommunicatorAction, Object, Object> oneArgument = CommunicatorAction::communicate;

        Map<MetaMethod<?>, CommunicatorAction> actions = proxyClass.methods()
                .stream()
                .filter(method -> method.parameters().size() < 2)
                .collect(mapCollector(identity(), provider));

        if (actions.size() != proxyClass.methods().size()) {
            String invalidMethods = joinMethods(proxyClass, actions::containsKey);
            throw new CommunicatorException(format(COMMUNICATOR_HAS_INVALID_METHOD_FOR_PROXY, proxyClass.definition().type().getName(), invalidMethods));
        }

        Map<MetaMethod<?>, Function<Object, Object>> invocations = actions
                .entrySet()
                .stream()
                .collect(mapCollector(Map.Entry::getKey, entry -> entry.getKey().parameters().isEmpty()
                        ? input -> noArguments.apply(entry.getValue())
                        : input -> oneArgument.apply(entry.getValue(), input)));

        MetaProxy proxy = proxyClass.proxy(invocations);

        return new CommunicatorProxy<>(cast(proxy), actions.values().stream().collect(immutableMapCollector(CommunicatorAction::getId, identity())));
    }
}
