package io.art.core.factory;


import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Arrays.*;
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

    public static <T> Stack<T> stackOf(ImmutableSet<T> elements) {
        return isEmpty(elements) ? stack() : stackOf(elements.toMutable());
    }

    public static <T> Stack<T> stackOf(ImmutableArray<T> elements) {
        return isEmpty(elements) ? stack() : stackOf(elements.toMutable());
    }

    @SafeVarargs
    public static <T> Stack<T> stackOf(T... elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<T> stack = new Stack<>();
        stack.addAll(asList(elements));
        return stack;
    }

    public static Stack<Long> stackOf(long[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Long> stack = new Stack<>();
        for (long element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Integer> stackOf(int[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Integer> stack = new Stack<>();
        for (int element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Byte> stackOf(byte[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Byte> stack = new Stack<>();
        for (byte element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Short> stackOf(short[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Short> stack = new Stack<>();
        for (short element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Character> stackOf(char[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Character> stack = new Stack<>();
        for (char element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Float> stackOf(float[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Float> stack = new Stack<>();
        for (float element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Double> stackOf(double[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Double> stack = new Stack<>();
        for (double element : elements) stack.add(element);
        return stack;
    }

    public static Stack<Boolean> stackOf(boolean[] elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<Boolean> stack = new Stack<>();
        for (boolean element : elements) stack.add(element);
        return stack;
    }
}
