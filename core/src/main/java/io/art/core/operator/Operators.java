package io.art.core.operator;

import lombok.experimental.*;
import java.util.function.*;

@UtilityClass
public class Operators {
    public <T> void applyIf(T value, Predicate<T> predicate, Consumer<T> action) {
        if (predicate.test(value)) {
            action.accept(value);
        }
    }
}
