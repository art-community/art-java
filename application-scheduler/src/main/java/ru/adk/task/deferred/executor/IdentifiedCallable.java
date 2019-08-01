package ru.adk.task.deferred.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.core.identified.UniqueIdentified;
import java.util.concurrent.Callable;

@Getter
@AllArgsConstructor
public class IdentifiedCallable<T> implements UniqueIdentified {
    private String id;
    private Callable<T> callable;
}
