package ru.art.core.constants;

import static java.lang.Runtime.getRuntime;

public interface ThreadConstants {
    int DEFAULT_THREAD_POOL_SIZE = getRuntime().availableProcessors() - 1;
}
