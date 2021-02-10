package io.art.core.operator;

import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import java.util.function.*;

@UtilityClass
public class Operators {
    public <T> void applyIf(T value, Predicate<T> predicate, Consumer<T> action) {
        if (predicate.test(value)) {
            action.accept(value);
        }
    }

    public <T> UnaryOperator<T> andThen(UnaryOperator<T> current, UnaryOperator<? extends T> after) {
        return cast(current.andThen(cast(after)));
    }
}
