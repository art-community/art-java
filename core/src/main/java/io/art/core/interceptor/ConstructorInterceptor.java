package io.art.core.interceptor;

import java.util.function.*;

public interface ConstructorInterceptor<T> extends Consumer<T> {
    default T intercept(Supplier<T> constructor) {
        T created = constructor.get();
        this.accept(created);
        return created;
    }
}
