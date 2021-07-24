package io.art.meta.invoker;


import io.art.meta.model.*;
import static io.art.core.singleton.SingletonsRegistry.*;
import java.util.function.*;

public class MetaMethodInvoker {
    private final Supplier<Object> invokeWithoutParameters;
    private final Function<Object, Object> invokeOneParameter;
    private final Function<Object[], Object> invokeWithParameters;

    public MetaMethodInvoker(MetaClass<?> owner, MetaMethod<Object> method) {
        if (method.isStatic()) {
            invokeWithoutParameters = ((StaticMetaMethod<?>) method)::invokeCatched;
            invokeOneParameter = ((StaticMetaMethod<?>) method)::invokeCatched;
            invokeWithParameters = ((StaticMetaMethod<?>) method)::invokeCatched;
            return;
        }
        Object singleton = singleton(owner.definition().type(), owner.creator().noPropertiesConstructor()::invokeCatched);
        invokeWithoutParameters = () -> ((InstanceMetaMethod<Object, Object>) method).invokeCatched(singleton);
        invokeOneParameter = argument -> ((InstanceMetaMethod<Object, Object>) method).invokeCatched(singleton, argument);
        invokeWithParameters = arguments -> ((InstanceMetaMethod<Object, Object>) method).invokeCatched(singleton, arguments);
    }

    public Object invoke() {
        return invokeWithoutParameters.get();
    }

    public Object invoke(Object argument) {
        return invokeOneParameter.apply(argument);
    }

    public Object invoke(Object[] arguments) {
        return invokeWithParameters.apply(arguments);
    }
}
