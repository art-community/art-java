package io.art.core.factory;

import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static java.util.Arrays.*;
import java.util.*;

@UtilityClass
public class QueueFactory {
    public static <T> Deque<T> deque() {
        return new LinkedList<>();
    }

    public static <T> Deque<T> dequeOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements);
    }

    @SafeVarargs
    public static <T> Deque<T> dequeOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }

    public static Deque<Long> dequeOf(long[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Long> deque = dequeOf();
        for (long element : elements) deque.add(element);
        return deque;
    }

    public static Deque<Integer> dequeOf(int[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Integer> deque = dequeOf();
        for (int element : elements) deque.add(element);
        return deque;
    }

    public static Deque<Byte> dequeOf(byte[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Byte> deque = dequeOf();
        for (byte element : elements) deque.add(element);
        return deque;
    }

    public static Deque<Double> dequeOf(double[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Double> deque = dequeOf();
        for (double element : elements) deque.add(element);
        return deque;
    }

    public static Deque<Float> dequeOf(float[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Float> deque = dequeOf();
        for (float element : elements) deque.add(element);
        return deque;
    }

    public static Deque<Boolean> dequeOf(boolean[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Boolean> deque = dequeOf();
        for (boolean element : elements) deque.add(element);
        return deque;
    }

    public static Deque<Short> dequeOf(short[] elements) {
        if (isEmpty(elements)) return dequeOf();
        Deque<Short> deque = dequeOf();
        for (short element : elements) deque.add(element);
        return deque;
    }



    public static <T> Queue<T> queue() {
        return new LinkedList<>();
    }

    @SafeVarargs
    public static <T> Queue<T> queueOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }

    public static Queue<Long> queueOf(long[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Long> queue = queueOf();
        for (long element : elements) queue.add(element);
        return queue;
    }

    public static Queue<Integer> queueOf(int[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Integer> queue = queueOf();
        for (int element : elements) queue.add(element);
        return queue;
    }

    public static Queue<Byte> queueOf(byte[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Byte> queue = queueOf();
        for (byte element : elements) queue.add(element);
        return queue;
    }

    public static Queue<Double> queueOf(double[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Double> queue = queueOf();
        for (double element : elements) queue.add(element);
        return queue;
    }

    public static Queue<Float> queueOf(float[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Float> queue = queueOf();
        for (float element : elements) queue.add(element);
        return queue;
    }

    public static Queue<Boolean> queueOf(boolean[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Boolean> deque = queueOf();
        for (boolean element : elements) deque.add(element);
        return deque;
    }

    public static Queue<Short> queueOf(short[] elements) {
        if (isEmpty(elements)) return queueOf();
        Queue<Short> deque = queueOf();
        for (short element : elements) deque.add(element);
        return deque;
    }

    public static <T> Queue<T> queueOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements);
    }



    public static <T> PriorityQueue<T> priorityQueue(Comparator<T> comparator) {
        return new PriorityQueue<>(comparator);
    }

    @SafeVarargs
    public static <T> PriorityQueue<T> priorityQueueOf(Comparator<T> comparator, T... elements) {
        PriorityQueue<T> queue = new PriorityQueue<>(comparator);
        queue.addAll(asList(elements));
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }

    public static <T> PriorityQueue<T> priorityQueueOf(Comparator<T> comparator, Collection<T> elements) {
        PriorityQueue<T> queue = new PriorityQueue<>(comparator);
        queue.addAll(elements);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }

    public static PriorityQueue<Long> priorityQueueOf(Comparator<Long> comparator, long[] elements) {
        PriorityQueue<Long> queue = new PriorityQueue<>(comparator);
        for (long element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }

    public static PriorityQueue<Integer> priorityQueueOf(Comparator<Integer> comparator, int[] elements) {
        PriorityQueue<Integer> queue = new PriorityQueue<>(comparator);
        for (int element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;

    }

    public static PriorityQueue<Byte> priorityQueueOf(Comparator<Byte> comparator, byte[] elements) {
        PriorityQueue<Byte> queue = new PriorityQueue<>(comparator);
        for (byte element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;

    }

    public static PriorityQueue<Double> priorityQueueOf(Comparator<Double> comparator, double[] elements) {
        PriorityQueue<Double> queue = new PriorityQueue<>(comparator);
        for (double element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;

    }

    public static PriorityQueue<Float> priorityQueueOf(Comparator<Float> comparator, float[] elements) {
        PriorityQueue<Float> queue = new PriorityQueue<>(comparator);
        for (float element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;

    }

    public static PriorityQueue<Boolean> priorityQueueOf(Comparator<Boolean> comparator, boolean[] elements) {
        PriorityQueue<Boolean> queue = new PriorityQueue<>(comparator);
        for (boolean element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }

    public static PriorityQueue<Short> priorityQueueOf(Comparator<Short> comparator, short[] elements) {
        PriorityQueue<Short> queue = new PriorityQueue<>(comparator);
        for (short element : elements) queue.add(element);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }
}
