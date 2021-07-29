package io.art.core.invoker;

import lombok.*;
import java.util.function.*;

@Builder
public class FunctionInvoker implements Invoker {
    private final Supplier<Object> noArgumentsFunction;
    private final Function<Object, Object> oneArgumentFunction;
    private final Function<Object[], Object> argumentsFunction;

    @Override
    public Object invoke() {
        return noArgumentsFunction.get();
    }

    @Override
    public Object invoke(Object argument) {
        return oneArgumentFunction.apply(argument);
    }

    @Override
    public Object invoke(Object[] arguments) {
        return argumentsFunction.apply(arguments);
    }
}
