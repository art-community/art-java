package io.art.meta.invoker;


import io.art.meta.model.*;
import lombok.*;
import static io.art.core.singleton.SingletonsRegistry.*;
import java.util.function.*;

public class MetaMethodInvoker {
    private final Supplier<Object> invokeWithoutParameters;
    private final Function<Object, Object> invokeOneParameter;
    private final Function<Object[], Object> invokeWithParameters;

    @Getter
    private final MetaClass<?> owner;

    @Getter
    private final MetaMethod<?> delegate;

    public MetaMethodInvoker(MetaClass<?> owner, MetaMethod<?> delegate) {
        this.owner = owner;
        this.delegate = delegate;
        if (delegate.isStatic()) {
            invokeWithoutParameters = ((StaticMetaMethod<?>) delegate)::invokeCatched;
            invokeOneParameter = ((StaticMetaMethod<?>) delegate)::invokeCatched;
            invokeWithParameters = ((StaticMetaMethod<?>) delegate)::invokeCatched;
            return;
        }
        Object singleton = singleton(owner.definition().type(), owner.creator().noPropertiesConstructor()::invokeCatched);
        invokeWithoutParameters = () -> ((InstanceMetaMethod<Object, Object>) delegate).invokeCatched(singleton);
        invokeOneParameter = argument -> ((InstanceMetaMethod<Object, Object>) delegate).invokeCatched(singleton, argument);
        invokeWithParameters = arguments -> ((InstanceMetaMethod<Object, Object>) delegate).invokeCatched(singleton, arguments);
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
