package ru.adk.task.deferred.executor.model

import java.time.LocalDateTime

class DeferredEventResult<T> {
    LocalDateTime triggeredTime
    int order
    T value

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        DeferredEventResult that = (DeferredEventResult) o

        if (order != that.order) return false
        if (that.triggeredTime.hour - triggeredTime.hour > 1
                || that.triggeredTime.minute - triggeredTime.minute > 1
                || that.triggeredTime.second - triggeredTime.second > 1) return false
        if (value != that.value) return false
        return true
    }
}