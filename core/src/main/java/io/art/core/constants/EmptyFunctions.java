package io.art.core.constants;

import java.util.function.*;

public interface EmptyFunctions {
    Runnable EMPTY_RUNNABLE = () -> {
    };
    Consumer<?> EMPTY_CONSUMER = (Object ignore) -> {
    };
    Supplier<?> EMPTY_SUPPLIER = () -> null;
    Function<?, ?> EMPTY_FUNCTION = (Object ignore) -> null;
}
