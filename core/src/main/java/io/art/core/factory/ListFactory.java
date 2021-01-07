package io.art.core.factory;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public class ListFactory {
    public static <T> CopyOnWriteArrayList<T> copyOnWriteList() {
        return new CopyOnWriteArrayList<>();
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteListOf(T element) {
        CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<>();
        list.add(element);
        return list;
    }

    public static <T> CopyOnWriteArrayList<T> copyOnWriteListOf(Collection<T> list) {
        return new CopyOnWriteArrayList<>(list);
    }


    public static <T> List<T> linkedList() {
        return new LinkedList<>();
    }

    @SafeVarargs
    public static <T> List<T> linkedListOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }

    public static <T> List<T> linkedListOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements);
    }

    public static <T> List<T> linkedListOf(ImmutableArray<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements.toMutable());
    }

    public static <T> List<T> linkedListOf(ImmutableSet<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements.toMutable());
    }

    public static List<Long> linkedListOf(long[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Long> array = linkedListOf();
        for (long element : elements) array.add(element);
        return array;
    }

    public static List<Short> linkedListOf(short[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Short> array = linkedListOf();
        for (short element : elements) array.add(element);
        return array;
    }

    public static List<Integer> linkedListOf(int[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Integer> array = linkedListOf();
        for (int element : elements) array.add(element);
        return array;
    }

    public static List<Byte> linkedListOf(byte[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Byte> array = linkedListOf();
        for (byte element : elements) array.add(element);
        return array;
    }

    public static List<Double> linkedListOf(double[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Double> array = linkedListOf();
        for (double element : elements) array.add(element);
        return array;
    }

    public static List<Float> linkedListOf(float[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Float> array = linkedListOf();
        for (float element : elements) array.add(element);
        return array;
    }

    public static List<Boolean> linkedListOf(boolean[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Boolean> array = linkedListOf();
        for (boolean element : elements) array.add(element);
        return array;
    }

    public static List<Character> linkedListOf(char[] elements) {
        if (isEmpty(elements)) return linkedListOf();
        List<Character> array = linkedListOf();
        for (char element : elements) array.add(element);
        return array;
    }
}
