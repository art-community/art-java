package ru.adk.task.deferred.executor.operation

import ru.adk.task.deferred.executor.DeferredExecutor
import ru.adk.task.deferred.executor.model.DeferredEventResult

import java.time.LocalDateTime
import java.util.concurrent.Callable
import java.util.concurrent.Future

import static java.lang.Thread.*
import static java.time.LocalDateTime.now
import static java.util.Comparator.comparing

class DeferredExecutorSpecOperations {
    static DeferredEventResult<?> createDeferredEventResult() {
        new DeferredEventResult<?>(triggeredTime: now())
    }

    static DeferredEventResult<?> createDeferredEventResult(LocalDateTime time, int order) {
        new DeferredEventResult<?>(order: order, triggeredTime: time)
    }

    static DeferredEventResult<?> createDeferredEventResult(LocalDateTime time, int order, returnValue) {
        new DeferredEventResult<?>(order: order, triggeredTime: time, value: returnValue)
    }

    static void addEventResult(Collection<DeferredEventResult> realResults) {
        realResults.add createDeferredEventResult(now(), realResults.size()); realResults.size()
    }

    static void addEventResult(Collection<DeferredEventResult<?>> realResults, returnValue) {
        realResults.add createDeferredEventResult(now(), realResults.size(), returnValue); realResults.size()
    }


    static Callable<DeferredEventResult> createDeferredEventTask(triggerTime, orderedTasksAppender) {
        return {
            println formatDeferredTaskExecutionMessage(triggerTime, now(), orderedTasksAppender() as int)
        }
    }

    static Callable<DeferredEventResult> createDeferredEventTask(triggerTime, orderedTasksAppender, value) {
        return {
            println "${formatDeferredTaskExecutionMessage(triggerTime, now(), orderedTasksAppender() as int)}. Value: $value"
            value
        }
    }

    static Callable<DeferredEventResult> createInfinityDeferredEventTask(triggerTime, orderedTasksAppender) {
        return {
            def order = orderedTasksAppender(createDeferredEventResult()) as int
            println formatDeferredTaskExecutionMessage(triggerTime, now(), order)
            while (!interrupted()) {
                try {
                    println "Infinity task with order: $order and trigger time: $triggerTime is working..."
                    sleep 1000
                } catch (e) {
                    println e
                }
            }
            println "Infinity task canceled"
        }
    }

    static Callable<DeferredEventResult> createLongDeferredEventTask(triggerTime, orderedTasksAppender) {
        return {
            def order = orderedTasksAppender(createDeferredEventResult()) as int
            println formatDeferredTaskExecutionMessage(triggerTime, now(), order)
            sleep 10000
            println "Long task with order: $order and trigger time: $triggerTime is working..."
        }
    }

    static PriorityQueue createExpectedResultsQueue() {
        new PriorityQueue(comparing({ (it as DeferredEventResult).triggeredTime }).thenComparingInt({ (it as DeferredEventResult).order }))
    }

    static String formatDeferredTaskExecutionMessage(expectedTriggerTime, realTriggerTime, order) {
        "Task executed on: $realTriggerTime. Scheduled time: $expectedTriggerTime. Thread: ${currentThread().name}. Task order: $order"
    }

    static Future<? extends DeferredEventResult> addEventToExecutor(DeferredExecutor executor, LocalDateTime triggerTime, Queue<DeferredEventResult> actualResults) {
        executor.submit createDeferredEventTask(triggerTime, { addEventResult actualResults; actualResults.size() - 1 }), triggerTime
    }

    static Future<? extends DeferredEventResult> addEventToExecutorWithReturningValue(DeferredExecutor executor, LocalDateTime triggerTime, Queue<DeferredEventResult> actualResults, value) {
        executor.submit createDeferredEventTask(triggerTime, { addEventResult actualResults, value; actualResults.size() - 1 }, value), triggerTime
    }


    static void runInJoinedThread(Closure<?> action) {
        Thread.start { action() }.join()
    }
}
