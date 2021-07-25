package io.art.communicator.proxy;

import io.art.communicator.action.*;
import io.art.core.exception.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.MapCollector.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class CommunicatorProxyFactory {
    public static <T> T communicatorProxy(MetaClass<T> proxyClass, Function<MetaMethod<?>, CommunicatorAction> provider) {
        Function<CommunicatorAction, Object> noArguments = CommunicatorAction::communicate;
        BiFunction<CommunicatorAction, Object, Object> oneArgument = CommunicatorAction::communicate;

        Map<MetaMethod<?>, CommunicatorAction> actions = proxyClass.methods()
                .stream()
                .filter(method -> method.parameters().size() < 2)
                .collect(mapCollector(identity(), provider));

        Map<MetaMethod<?>, Function<Object, Object>> invocations = actions
                .entrySet()
                .stream()
                .collect(mapCollector(Map.Entry::getKey, entry -> entry.getKey().parameters().isEmpty()
                        ? input -> noArguments.apply(entry.getValue())
                        : input -> oneArgument.apply(entry.getValue(), input)));

        MetaProxy proxy = proxyClass.proxy(invocations);

        if (isNull(proxy)) {
            throw new ImpossibleSituationException();
        }

        return cast(proxy);
    }
}
