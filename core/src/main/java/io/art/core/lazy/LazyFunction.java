package io.art.core.lazy;

import lombok.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class LazyFunction<X, Y> {
    private final AtomicReference<Y> value = new AtomicReference<>();
    private final Function<X, Y> loader;

    public Y apply(X argument) {
        Y value;
        if (nonNull(value = this.value.get())) {
            return value;
        }
        if (this.value.compareAndSet(null, value = loader.apply(argument))) {
            return value;
        }
        return this.value.get();
    }

    public static <X, Y> LazyFunction<X, Y> lazy(Function<X, Y> functor) {
        return new LazyFunction<>(functor);
    }
}
