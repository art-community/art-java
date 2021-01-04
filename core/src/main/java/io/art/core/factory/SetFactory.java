package io.art.core.factory;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public class SetFactory {
    public static <T> Set<T> set() {
        return new LinkedHashSet<>();
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(asList(elements));
    }

    public static <T> Set<T> setOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(elements);
    }

    public static Set<Long> setOf(long[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Long> set = setOf();
        for (long element : elements) set.add(element);
        return set;
    }

    public static Set<Integer> setOf(int[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Integer> set = setOf();
        for (int element : elements) set.add(element);
        return set;
    }

    public static Set<Byte> setOf(byte[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Byte> set = setOf();
        for (byte element : elements) set.add(element);
        return set;
    }

    public static Set<Double> setOf(double[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Double> set = setOf();
        for (double element : elements) set.add(element);
        return set;
    }

    public static Set<Float> setOf(float[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Float> set = setOf();
        for (float element : elements) set.add(element);
        return set;
    }

    public static Set<Boolean> setOf(boolean[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Boolean> set = setOf();
        for (boolean element : elements) set.add(element);
        return set;
    }

    public static Set<Short> setOf(short[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Short> set = setOf();
        for (short element : elements) set.add(element);
        return set;
    }


    @SafeVarargs
    public static <T> ImmutableSet<T> immutableSetOf(T... elements) {
        return new ImmutableSet<>(asList(elements));
    }

    public static <T> ImmutableSet<T> immutableSetOf(Iterable<T> elements) {
        return new ImmutableSet<>(elements);
    }


    public static <T> Set<T> concurrentHashSet() {
        return ConcurrentHashMap.newKeySet();
    }

    @SafeVarargs
    public static <T> Set<T> concurrentHashSetOf(T... value) {
        ConcurrentHashMap.KeySetView<T, Boolean> set = ConcurrentHashMap.newKeySet();
        set.addAll(asList(value));
        return set;
    }

    public static <T> Set<T> concurrentHashSetOf(Collection<T> elements) {
        ConcurrentHashMap.KeySetView<T, Boolean> set = ConcurrentHashMap.newKeySet();
        set.addAll(elements);
        return set;
    }


    public static <T> CopyOnWriteArraySet<T> copyOnWriteSet() {
        return new CopyOnWriteArraySet<>();
    }

    @SafeVarargs
    public static <T> CopyOnWriteArraySet<T> copyOnWriteSet(T... element) {
        return new CopyOnWriteArraySet<>(asList(element));
    }

    public static <T> CopyOnWriteArraySet<T> copyOnWriteSetOf(Collection<T> collection) {
        return new CopyOnWriteArraySet<>(collection);
    }
}
