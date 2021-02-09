package io.art.extensions

import io.art.core.callable.ExceptionCallable
import io.art.scheduler.manager.SchedulersManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.concurrent.Callable


inline fun <reified T> schedule(startTime: LocalDateTime, task: Callable<T>) = SchedulersManager.schedule(task, startTime)!!

inline fun <reified T> schedule(task: Callable<T>) = SchedulersManager.schedule(task, now())!!


inline fun <reified T> scheduleFixedRate(startTime: LocalDateTime, period: Duration, task: ExceptionCallable<out T>) = SchedulersManager.scheduleFixedRate(task, startTime, period)

inline fun <reified T> scheduleFixedRate(period: Duration, task: ExceptionCallable<out T>) = SchedulersManager.scheduleFixedRate(task, now(), period)


inline fun <reified T> scheduleDelayed(startTime: LocalDateTime, period: Duration, task: ExceptionCallable<out T>) = SchedulersManager.scheduleDelayed(task, startTime, period)

inline fun <reified T> scheduleDelayed(period: Duration, task: ExceptionCallable<out T>) = SchedulersManager.scheduleDelayed(task, now(), period)
