package io.art.scheduler.queue;

import io.art.core.extensions.*;
import lombok.*;
import static io.art.core.factory.QueueFactory.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.*;

@RequiredArgsConstructor
public class PriorityWaitingQueue<T> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();
    private boolean terminated = false;

    private final int maximumCapacity;
    private final Consumer<T> eraser;
    private final PriorityQueue<T> queue;

    public PriorityWaitingQueue(int maximumCapacity, Consumer<T> eraser, Comparator<T> comparator) {
        this.maximumCapacity = maximumCapacity;
        this.eraser = eraser;
        queue = priorityQueue(comparator);
    }

    public boolean offer(T element) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (terminated) {
                return false;
            }

            if (queue.size() + 1 > maximumCapacity) {
                return false;
            }

            queue.offer(element);
            available.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        final ReentrantLock lock = this.lock;
        try {
            lock.lockInterruptibly();
            T result;
            while (isNull(result = queue.poll()) && !terminated) {
                available.await();
            }
            if (terminated) {
                CollectionExtensions.erase(queue, eraser);
                return null;
            }
            return result;
        } catch (InterruptedException interruptedException) {
            erase();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void terminate() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            terminated = true;
            available.signal();
        } finally {
            lock.unlock();
        }
    }

    public void erase() {
        if (terminated) {
            CollectionExtensions.erase(queue, eraser);
        }
    }
}
