package io.art.core.factory;


import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static java.util.Arrays.asList;
import java.util.*;

@UtilityClass
public class StackFactory {
    public static <T> Stack<T> stack() {
        return new Stack<>();
    }

    public static <T> Stack<T> stackOf(Collection<T> elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<T> stack = new Stack<>();
        stack.addAll(elements);
        return stack;
    }

    @SafeVarargs
    public static <T> Stack<T> stackOf(T... elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<T> stack = new Stack<>();
        stack.addAll(asList(elements));
        return stack;
    }
}
