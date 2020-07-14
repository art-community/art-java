/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.task.deferred.executor;

import java.time.*;
import java.util.concurrent.*;

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
