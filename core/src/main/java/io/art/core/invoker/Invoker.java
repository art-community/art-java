package io.art.core.invoker;

public interface Invoker {
    Object invoke();

    Object invoke(Object argument);

    Object invoke(Object[] arguments);
}
