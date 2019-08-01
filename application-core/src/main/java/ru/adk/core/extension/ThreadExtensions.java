package ru.adk.core.extension;

public interface ThreadExtensions {
    static void thread(String name, Runnable runnable) {
        new Thread(runnable, name).start();
    }

    static void thread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
