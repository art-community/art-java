package io.art.core.extensions;

import io.art.core.checker.*;
import lombok.experimental.*;
import java.util.function.*;

@UtilityClass
public class FunctionExtensions {
    public static <T> T apply(T target, Consumer<T> action) {
        action.accept(target);
        return target;
    }

    public static <T> T applyIf(T target, Predicate<T> predicate, Consumer<T> action) {
        if (predicate.test(target)) {
            action.accept(target);
        }
        return target;
    }

    public static <T> UnaryOperator<T> then(UnaryOperator<T> current, UnaryOperator<T> next) {
        return value -> next.apply(current.apply(value));
    }

    public static Runnable before(Runnable before, Runnable current) {
        return () -> {
            NullityChecker.apply(before, Runnable::run);
            NullityChecker.apply(current, Runnable::run);
        };
    }
}
