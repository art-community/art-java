package io.art.meta.invoker;


import io.art.core.invoker.*;
import io.art.meta.exception.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.singleton.SingletonsRegistry.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.constants.MetaConstants.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.util.function.*;

public class MetaMethodInvoker implements Invoker {
    private final Supplier<Object> invokeWithoutParameters;
    private final Function<Object, Object> invokeOneParameter;
    private final Function<Object[], Object> invokeWithParameters;

    @Getter
    private final MetaClass<?> owner;

    @Getter
    private final MetaMethod<MetaClass<?>, ?> delegate;

    public MetaMethodInvoker(MetaClass<?> owner, MetaMethod<MetaClass<?>, ?> delegate) {
        this.owner = owner;
        this.delegate = delegate;
        if (delegate.isStatic()) {
            invokeWithoutParameters = ((StaticMetaMethod<MetaClass<?>, ?>) delegate)::invokeCatched;
            invokeOneParameter = ((StaticMetaMethod<MetaClass<?>, ?>) delegate)::invokeCatched;
            invokeWithParameters = ((StaticMetaMethod<MetaClass<?>, ?>) delegate)::invokeCatched;
            return;
        }
        MetaConstructor<MetaClass<?>, ?> constructor = owner.creator().noPropertiesConstructor();
        if (isNull(constructor)) {
            throw new MetaException(format(UNABLE_TO_CREATE_SINGLETON, owner.definition().type()));
        }
        Object singleton = singleton(owner.definition().type(), constructor::invokeCatched);
        invokeWithoutParameters = () -> ((InstanceMetaMethod<MetaClass<?>, ?, ?>) delegate).invokeCatched(cast(singleton));
        invokeOneParameter = argument -> ((InstanceMetaMethod<MetaClass<?>, ?, ?>) delegate).invokeCatched(cast(singleton), argument);
        invokeWithParameters = arguments -> ((InstanceMetaMethod<MetaClass<?>, ?, ?>) delegate).invokeCatched(cast(singleton), arguments);
    }

    @Override
    public Object invoke() {
        return invokeWithoutParameters.get();
    }

    @Override
    public Object invoke(Object argument) {
        return invokeOneParameter.apply(argument);
    }

    @Override
    public Object invoke(Object[] arguments) {
        return invokeWithParameters.apply(arguments);
    }

    @Override
    public String toString() {
        return format(INVOKER_FORMAT, owner.definition(), delegate);
    }
}
