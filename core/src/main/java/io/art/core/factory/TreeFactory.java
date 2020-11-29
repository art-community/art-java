package io.art.core.factory;

import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Arrays.*;
import java.util.*;

@UtilityClass
public class TreeFactory {
    public static <T extends Comparable<T>> TreeSet<T> tree() {
        return new TreeSet<>();
    }

    @SafeVarargs
    public static <T> TreeSet<T> treeOf(Comparator<T> comparator, T... elements) {
        TreeSet<T> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        treeSet.addAll(asList(elements));
        return treeSet;
    }

    @SafeVarargs
    public static <T extends Comparable<T>> TreeSet<T> treeOf(T... elements) {
        TreeSet<T> treeSet = new TreeSet<>();
        if (isEmpty(elements)) return treeSet;
        treeSet.addAll(asList(elements));
        return treeSet;
    }

    public static <T> TreeSet<T> treeOf(Collection<T> elements, Comparator<T> comparator) {
        TreeSet<T> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        treeSet.addAll(elements);
        return treeSet;
    }

    public static TreeSet<Long> treeOf(Comparator<Long> comparator, long[] elements) {
        TreeSet<Long> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (long element : elements) treeSet.add(element);
        return treeSet;
    }

    public static TreeSet<Integer> treeOf(Comparator<Integer> comparator, int[] elements) {
        TreeSet<Integer> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (int element : elements) treeSet.add(element);
        return treeSet;
    }

    public static TreeSet<Byte> treeOf(Comparator<Byte> comparator, byte[] elements) {
        TreeSet<Byte> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (byte element : elements) treeSet.add(element);
        return treeSet;
    }

    public static TreeSet<Double> treeOf(Comparator<Double> comparator, double[] elements) {
        TreeSet<Double> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (double element : elements) treeSet.add(element);
        return treeSet;
    }

    public static TreeSet<Float> treeOf(Comparator<Float> comparator, float[] elements) {
        TreeSet<Float> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (float element : elements) treeSet.add(element);
        return treeSet;
    }

    public static TreeSet<Boolean> treeOf(Comparator<Boolean> comparator, boolean[] elements) {
        TreeSet<Boolean> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (boolean element : elements) treeSet.add(element);
        return treeSet;
    }

    public static TreeSet<Short> treeOf(Comparator<Short> comparator, short[] elements) {
        TreeSet<Short> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        for (short element : elements) treeSet.add(element);
        return treeSet;
    }
}
