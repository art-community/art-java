package io.art.core.factory;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public class SetFactory {
    public static <T> Set<T> set() {
        return new LinkedHashSet<>();
    }

    public static <T> Set<T> setOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(elements);
    }

    public static <T> Set<T> setOf(ImmutableSet<T> elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(elements.toMutable());
    }

    public static <T> Set<T> setOf(ImmutableArray<T> elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(elements.toMutable());
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

    public static Set<Character> setOf(char[] elements) {
        if (isEmpty(elements)) return setOf();
        Set<Character> set = setOf();
        for (char element : elements) set.add(element);
        return set;
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T... elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(asList(elements));
    }


    @SafeVarargs
    public static <T> ImmutableSet<T> immutableSetOf(T... elements) {
        return new ImmutableSetImplementation<>(asList(elements));
    }

    public static <T> ImmutableSet<T> immutableSetOf(Iterable<T> elements) {
        return new ImmutableSetImplementation<>(elements);
    }

    public static ImmutableSet<Long> immutableSetOf(long[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Long> builder = immutableSetBuilder();
        for (long element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Integer> immutableSetOf(int[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Integer> builder = immutableSetBuilder();
        for (int element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Byte> immutableSetOf(byte[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Byte> builder = immutableSetBuilder();
        for (byte element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Double> immutableSetOf(double[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Double> builder = immutableSetBuilder();
        for (double element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Float> immutableSetOf(float[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Float> builder = immutableSetBuilder();
        for (float element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Boolean> immutableSetOf(boolean[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Boolean> builder = immutableSetBuilder();
        for (boolean element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Short> immutableSetOf(short[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Short> builder = immutableSetBuilder();
        for (short element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableSet<Character> immutableSetOf(char[] elements) {
        if (isEmpty(elements)) return emptyImmutableSet();
        ImmutableSet.Builder<Character> builder = immutableSetBuilder();
        for (char element : elements) builder.add(element);
        return builder.build();
    }


    public static <T> Set<T> concurrentSet() {
        return ConcurrentHashMap.newKeySet();
    }

    public static <T> Set<T> concurrentSet(int capacity) {
        return ConcurrentHashMap.newKeySet(capacity);
    }

    public static <T> Set<T> concurrentSetOf(Collection<T> elements) {
        ConcurrentHashMap.KeySetView<T, Boolean> set = ConcurrentHashMap.newKeySet();
        set.addAll(elements);
        return set;
    }


    public static <T> Set<T> copyOnWriteSet() {
        return new CopyOnWriteArraySet<>();
    }

    public static <T> Set<T> copyOnWriteSetOf(Collection<T> collection) {
        return new CopyOnWriteArraySet<>(collection);
    }
}
