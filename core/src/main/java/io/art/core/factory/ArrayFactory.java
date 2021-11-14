package io.art.core.factory;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collector.ArrayCollector.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.ArrayList;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

@UtilityClass
public class ArrayFactory {
    public static <T> List<T> fixedArrayOf(Collection<T> elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static <T> List<T> fixedArrayOf(ImmutableArray<T> elements) {
        return isEmpty(elements) ? emptyList() : elements.toMutable();
    }

    public static <T> List<T> fixedArrayOf(ImmutableSet<T> elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements.toMutable());
    }

    public static <T> List<T> fixedArrayOf(Stream<T> stream) {
        return isEmpty(stream) ? emptyList() : dynamicArrayOf(stream);
    }

    public static List<Long> fixedArrayOf(long[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Short> fixedArrayOf(short[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Integer> fixedArrayOf(int[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Byte> fixedArrayOf(byte[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Double> fixedArrayOf(double[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Float> fixedArrayOf(float[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Boolean> fixedArrayOf(boolean[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    public static List<Character> fixedArrayOf(char[] elements) {
        return isEmpty(elements) ? emptyList() : dynamicArrayOf(elements);
    }

    @SafeVarargs
    public static <T> List<T> fixedArrayOf(T... elements) {
        return isEmpty(elements) ? emptyList() : asList(elements);
    }


    public static <T> ImmutableArray<T> immutableArrayOf(Iterable<T> elements) {
        return isEmpty(elements) ? emptyImmutableArray() : new ImmutableArrayImplementation<>(elements);
    }

    public static <T> ImmutableArray<T> immutableSortedArrayOf(Iterable<T> elements, Comparator<T> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(elements, comparator);
    }


    public static <T> ImmutableArray<T> immutableSortedArrayOf(Stream<T> stream, Comparator<T> comparator) {
        return isEmpty(stream) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(stream), comparator);
    }

    public static ImmutableArray<Long> immutableSortedArrayOf(long[] elements, Comparator<Long> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Short> immutableSortedArrayOf(short[] elements, Comparator<Short> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Integer> immutableSortedArrayOf(int[] elements, Comparator<Integer> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Byte> immutableSortedArrayOf(byte[] elements, Comparator<Byte> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Double> immutableSortedArrayOf(double[] elements, Comparator<Double> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Float> immutableSortedArrayOf(float[] elements, Comparator<Float> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Boolean> immutableSortedArrayOf(boolean[] elements, Comparator<Boolean> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    public static ImmutableArray<Character> immutableSortedArrayOf(char[] elements, Comparator<Character> comparator) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }

    @SafeVarargs
    public static <T> ImmutableArray<T> immutableSortedArrayOf(Comparator<T> comparator, T... elements) {
        return isEmpty(elements) ? emptyImmutableArray() : immutableSortedArray(fixedArrayOf(elements), comparator);
    }


    public static <T> ImmutableArray<T> immutableArrayOf(Stream<T> stream) {
        return isNull(stream) ? emptyImmutableArray() : stream.collect(immutableArrayCollector());
    }

    public static ImmutableArray<Long> immutableArrayOf(long[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Long> builder = immutableArrayBuilder();
        for (long element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Short> immutableArrayOf(short[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Short> builder = immutableArrayBuilder();
        for (short element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Integer> immutableArrayOf(int[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Integer> builder = immutableArrayBuilder();
        for (int element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Byte> immutableArrayOf(byte[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Byte> builder = immutableArrayBuilder();
        for (byte element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Double> immutableArrayOf(double[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Double> builder = immutableArrayBuilder();
        for (double element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Float> immutableArrayOf(float[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Float> builder = immutableArrayBuilder();
        for (float element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Boolean> immutableArrayOf(boolean[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Boolean> builder = immutableArrayBuilder();
        for (boolean element : elements) builder.add(element);
        return builder.build();
    }

    public static ImmutableArray<Character> immutableArrayOf(char[] elements) {
        if (isEmpty(elements)) return emptyImmutableArray();
        Builder<Character> builder = immutableArrayBuilder();
        for (char element : elements) builder.add(element);
        return builder.build();
    }

    @SafeVarargs
    public static <T> ImmutableArray<T> immutableArrayOf(T... elements) {
        return isEmpty(elements) ? emptyImmutableArray() : new ImmutableArrayImplementation<>(asList(elements));
    }

    public static <T> ImmutableArray<T> immutableLazyArray(IntFunction<T> provider, int size) {
        return new ImmutableLazyArrayImplementation<>(provider, size);
    }

    public static <T> ImmutableArray<T> immutableLazyArrayOf(List<?> list, Function<?, T> mapper) {
        return immutableLazyArray(index -> mapper.apply(cast(list.get(index))), list.size());
    }

    public static <T> ImmutableArray<T> immutableLazyArrayOf(ImmutableArray<?> list, Function<?, T> mapper) {
        return immutableLazyArray(index -> mapper.apply(cast(list.get(index))), list.size());
    }

    public static <T> List<T> dynamicArray() {
        return new ArrayList<>();
    }

    public static <T> List<T> dynamicArray(int size) {
        return size <= 0 ? new ArrayList<>() : new ArrayList<>(size);
    }

    public static <T> List<T> dynamicArrayOf(Stream<T> stream) {
        return isEmpty(stream) ? new ArrayList<>() : stream.collect(arrayCollector());
    }

    public static <T> List<T> dynamicArrayOf(Collection<T> elements) {
        return isEmpty(elements) ? new ArrayList<>() : new ArrayList<>(elements);
    }

    public static <T> List<T> dynamicArrayOf(ImmutableArray<T> elements) {
        return isEmpty(elements) ? new ArrayList<>() : new ArrayList<>(elements.toMutable());
    }

    public static <T> List<T> dynamicArrayOf(ImmutableSet<T> elements) {
        return isEmpty(elements) ? new ArrayList<>() : new ArrayList<>(elements.toMutable());
    }

    public static <T> List<T> dynamicArrayOf(Iterable<T> elements) {
        if (isEmpty(elements)) {
            return dynamicArray();
        }
        List<T> array = dynamicArray();
        for (T element : elements) {
            array.add(element);
        }
        return array;
    }


    public static List<Long> dynamicArrayOf(long[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Long> array = dynamicArray(elements.length);
        for (long element : elements) array.add(element);
        return array;
    }

    public static List<Short> dynamicArrayOf(short[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Short> array = dynamicArray(elements.length);
        for (short element : elements) array.add(element);
        return array;
    }

    public static List<Integer> dynamicArrayOf(int[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Integer> array = dynamicArray(elements.length);
        for (int element : elements) array.add(element);
        return array;
    }

    public static List<Byte> dynamicArrayOf(byte[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Byte> array = dynamicArray(elements.length);
        for (byte element : elements) array.add(element);
        return array;
    }

    public static List<Double> dynamicArrayOf(double[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Double> array = dynamicArray(elements.length);
        for (double element : elements) array.add(element);
        return array;
    }

    public static List<Float> dynamicArrayOf(float[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Float> array = dynamicArray(elements.length);
        for (float element : elements) array.add(element);
        return array;
    }

    public static List<Boolean> dynamicArrayOf(boolean[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Boolean> array = dynamicArray(elements.length);
        for (boolean element : elements) array.add(element);
        return array;
    }

    public static List<Character> dynamicArrayOf(char[] elements) {
        if (isEmpty(elements)) return emptyList();
        List<Character> array = dynamicArray(elements.length);
        for (char element : elements) array.add(element);
        return array;
    }

    @SafeVarargs
    public static <T> List<T> dynamicArrayOf(T... elements) {
        return isEmpty(elements) ? new ArrayList<>() : new ArrayList<>(asList(elements));
    }


    @SafeVarargs
    public static <T> Stream<T> streamOf(T... elements) {
        return fixedArrayOf(elements).stream();
    }

    public static <T> Stream<T> streamOf(Collection<T> elements) {
        return fixedArrayOf(elements).stream();
    }

    public static <T> Stream<T> streamOf(ImmutableArray<T> elements) {
        return fixedArrayOf(elements).stream();
    }

    public static <T> Stream<T> streamOf(ImmutableSet<T> elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Long> streamOf(long[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Short> streamOf(short[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Integer> streamOf(int[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Byte> streamOf(byte[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Double> streamOf(double[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Float> streamOf(float[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Boolean> streamOf(boolean[] elements) {
        return fixedArrayOf(elements).stream();
    }

    public static Stream<Character> streamOf(char[] elements) {
        return fixedArrayOf(elements).stream();
    }


    @SafeVarargs
    public static <T> T[] arrayOf(T... elements) {
        return elements;
    }

}
