package io.art.communicator.proxy;

import io.art.communicator.action.*;
import io.art.core.exception.*;
import io.art.meta.model.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.MapCollector.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.util.function.*;

@UtilityClass
public class CommunicatorProxyFactory {
    public static <T> T communicatorProxy(MetaClass<T> proxyClass, Function<MetaMethod<?>, CommunicatorAction> provider) {
        Function<CommunicatorAction, Object> noArguments = CommunicatorAction::communicate;
        BiFunction<CommunicatorAction, Object, Object> oneArguments = CommunicatorAction::communicate;
        MetaProxy proxy = proxyClass.proxy(proxyClass.methods()
                .stream()
                .filter(method -> method.parameters().size() < 2)
                .collect(mapCollector(identity(), method -> method.parameters().isEmpty()
                        ? input -> noArguments.apply(provider.apply(method))
                        : input -> oneArguments.apply(provider.apply(method), input))));
        if (isNull(proxy)) {
            throw new ImpossibleSituationException();
        }
        return cast(proxy);
    }
}
