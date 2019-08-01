package ru.art.task.deferred.executor;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Интерфейс, описывающий ключевой компонент проекта
 */
public interface DeferredExecutor {
    /**
     * Добавляет новое отложенное событие в очередь
     *
     * @param eventTask         Задача, которая будет выполнена в указанные дата и время
     * @param triggerTime       Дата и время выполнения отложенного события
     * @param <EventResultType> Тип возвращаемого события выполняемой задачи
     * @see FutureTask
     */
    <EventResultType> Future<? extends EventResultType> submit(Callable<? extends EventResultType> eventTask, LocalDateTime triggerTime);

    /**
     * Добавляет новое отложенное событие в очередь и выполняет его немедленно
     *
     * @param eventTask         Задача, которая будет выполнена в указанные дату и время
     * @param <EventResultType> Тип возвращаемого события выполняемой задачи
     * @see FutureTask
     */
    <EventResultType> Future<? extends EventResultType> submit(Callable<? extends EventResultType> eventTask);

    /**
     * Добавляет новое отложенное событие в очередь и выполняет его немедленно
     *
     * @param task        Команда, которая будет выполнена в указанные дату и время
     * @param triggerTime Дата и время выполнения отложенного события
     * @see FutureTask
     */
    Future<?> execute(Runnable task, LocalDateTime triggerTime);

    Future<?> execute(Runnable task);

    /**
     * Прерывает выполнение всех событий
     */
    void shutdown();

    void clear();
}
