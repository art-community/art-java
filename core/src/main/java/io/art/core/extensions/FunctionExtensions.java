package io.art.core.extensions;

import lombok.experimental.*;
import java.util.function.*;

@UtilityClass
public class FunctionExtensions {
    public <T> T apply(T target, Consumer<T> action) {
        action.accept(target);
        return target;
    }

    public <T> UnaryOperator<T> then(UnaryOperator<T> current, UnaryOperator<T> next) {
        return value -> next.apply(current.apply(value));
    }

}
