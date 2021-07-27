package io.art.meta.invoker;


import io.art.meta.exception.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.singleton.SingletonsRegistry.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
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
        MetaConstructor<?> constructor = owner.creator().noPropertiesConstructor();
        if (isNull(constructor)) {
            throw new MetaException(format(UNABLE_TO_CREATE_SINGLETON, owner.definition().type().getName()));
        }
        Object singleton = singleton(owner.definition().type(), constructor::invokeCatched);
        invokeWithoutParameters = () -> ((InstanceMetaMethod<?, ?>) delegate).invokeCatched(cast(singleton));
        invokeOneParameter = argument -> ((InstanceMetaMethod<?, ?>) delegate).invokeCatched(cast(singleton), argument);
        invokeWithParameters = arguments -> ((InstanceMetaMethod<?, ?>) delegate).invokeCatched(cast(singleton), arguments);
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
