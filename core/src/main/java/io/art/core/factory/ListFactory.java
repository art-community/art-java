package io.art.core.factory;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public class ListFactory {
    public static <T> List<T> copyOnWriteList() {
        return new CopyOnWriteArrayList<>();
    }

    @SafeVarargs
    public static <T> List<T> copyOnWriteListOf(T... elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<T> array = copyOnWriteList();
        array.addAll(asList(elements));
        return array;
    }

    public static <T> List<T> copyOnWriteListOf(Collection<T> list) {
        return new CopyOnWriteArrayList<>(list);
    }

    public static <T> List<T> copyOnWriteListOf(ImmutableArray<T> elements) {
        return isEmpty(elements) ? copyOnWriteList() : copyOnWriteListOf(elements.toMutable());
    }

    public static <T> List<T> copyOnWriteListOf(ImmutableSet<T> elements) {
        return isEmpty(elements) ? copyOnWriteList() : copyOnWriteListOf(elements.toMutable());
    }

    public static List<Long> copyOnWriteListOf(long[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Long> array = copyOnWriteList();
        for (long element : elements) array.add(element);
        return array;
    }

    public static List<Short> copyOnWriteListOf(short[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Short> array = copyOnWriteList();
        for (short element : elements) array.add(element);
        return array;
    }

    public static List<Integer> copyOnWriteListOf(int[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Integer> array = copyOnWriteList();
        for (int element : elements) array.add(element);
        return array;
    }

    public static List<Byte> copyOnWriteListOf(byte[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Byte> array = copyOnWriteList();
        for (byte element : elements) array.add(element);
        return array;
    }

    public static List<Double> copyOnWriteListOf(double[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Double> array = copyOnWriteList();
        for (double element : elements) array.add(element);
        return array;
    }

    public static List<Float> copyOnWriteListOf(float[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Float> array = copyOnWriteList();
        for (float element : elements) array.add(element);
        return array;
    }

    public static List<Boolean> copyOnWriteListOf(boolean[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Boolean> array = copyOnWriteList();
        for (boolean element : elements) array.add(element);
        return array;
    }

    public static List<Character> copyOnWriteListOf(char[] elements) {
        if (isEmpty(elements)) return copyOnWriteList();
        List<Character> array = copyOnWriteList();
        for (char element : elements) array.add(element);
        return array;
    }


    public static <T> List<T> linkedList() {
        return new LinkedList<>();
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

    @SafeVarargs
    public static <T> List<T> linkedListOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }
}
