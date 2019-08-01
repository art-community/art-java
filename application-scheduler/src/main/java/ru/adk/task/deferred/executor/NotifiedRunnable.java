package ru.adk.task.deferred.executor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class NotifiedRunnable implements Runnable {
    private Runnable executionCallable;
    private Runnable notification;

    @Override
    public void run() {
        executionCallable.run();
        notification.run();
    }
}
