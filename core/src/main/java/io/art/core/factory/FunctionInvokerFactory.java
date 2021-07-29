package io.art.core.factory;

import io.art.core.invoker.*;
import lombok.experimental.*;
import java.util.function.*;

@UtilityClass
public class FunctionInvokerFactory {
    public static FunctionInvoker voidInvoker(Runnable voidFunction) {
        return noArgumentsInvoker((() -> {
            voidFunction.run();
            return null;
        }));
    }

    public static FunctionInvoker noArgumentsInvoker(Supplier<Object> noArgumentsFunction) {
        return FunctionInvoker.builder().noArgumentsFunction(noArgumentsFunction).build();
    }

    public static FunctionInvoker oneArgumentInvoker(Function<Object, Object> oneArgumentFunction) {
        return FunctionInvoker.builder().oneArgumentFunction(oneArgumentFunction).build();
    }

    public static FunctionInvoker invoker(Function<Object[], Object> argumentsFunction) {
        return FunctionInvoker.builder().argumentsFunction(argumentsFunction).build();
    }
}
