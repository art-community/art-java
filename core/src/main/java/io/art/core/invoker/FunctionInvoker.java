package io.art.core.invoker;

import lombok.*;
import java.util.function.*;

@Builder
public class FunctionInvoker implements Invoker {
    private final Supplier<Object> noArguments;
    private final Function<Object, Object> oneArgument;
    private final Function<Object[], Object> manyArguments;

    @Override
    public Object invoke() {
        return noArguments.get();
    }

    @Override
    public Object invoke(Object argument) {
        return oneArgument.apply(argument);
    }

    @Override
    public Object invoke(Object[] arguments) {
        return manyArguments.apply(arguments);
    }
}
