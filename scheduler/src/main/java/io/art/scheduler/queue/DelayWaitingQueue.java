package io.art.scheduler.queue;

import io.art.core.extensions.*;
import lombok.*;
import static io.art.core.factory.QueueFactory.*;
import static java.lang.Thread.*;
import static java.util.Objects.*;
import static java.util.concurrent.TimeUnit.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.function.*;

@RequiredArgsConstructor
public class DelayWaitingQueue<T extends Delayed> {
    private final PriorityQueue<T> queue = priorityQueue(Delayed::compareTo);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition available = lock.newCondition();
    private boolean terminated = false;
    private Thread leader = null;

    private final int maximumCapacity;
    private final Consumer<T> eraser;

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
            if (queue.peek() == element) {
                leader = null;
                available.signal();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        final ReentrantLock lock = this.lock;
        try {
            lock.lockInterruptibly();
            for (; ; ) {
                if (terminated) {
                    return null;
                }

                T first = queue.peek();

                if (isNull(first)) {
                    available.await();
                    continue;
                }

                long delay = first.getDelay(NANOSECONDS);
                if (delay <= 0L) {
                    return queue.poll();
                }

                if (leader != null) {
                    available.await();
                    continue;
                }

                Thread thisThread = currentThread();
                leader = thisThread;
                try {
                    available.awaitNanos(delay);
                } finally {
                    if (leader == thisThread) {
                        leader = null;
                    }
                }
            }
        } catch (InterruptedException interruptedException) {
            CollectionExtensions.erase(queue, eraser);
            return null;
        } finally {
            if (isNull(leader) && nonNull(queue.peek())) {
                available.signal();
            }
            lock.unlock();
        }
    }

    public void terminate() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (terminated) return;
            terminated = true;
            available.signal();
        } finally {
            lock.unlock();
        }
    }

    public void erase() {
        lock.lock();
        try {
            CollectionExtensions.erase(queue, eraser);
        } finally {
            lock.unlock();
        }
    }
}
