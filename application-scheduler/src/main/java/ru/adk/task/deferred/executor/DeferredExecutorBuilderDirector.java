package ru.adk.task.deferred.executor;

/**
 * Директор создания обработчика отложенных событий
 * Используется для получения набора готовых объектов с разными наборами свойств
 * В данный момент создаёт обработчик отложенных событий с дефолтной конфигурацией
 */
public class DeferredExecutorBuilderDirector {
    public static DeferredExecutor createDefaultDeferredExecutor() {
        return DeferredExecutorImpl.builder().build();
    }
}
