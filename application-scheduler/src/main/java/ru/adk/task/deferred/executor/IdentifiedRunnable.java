package ru.adk.task.deferred.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.core.identified.UniqueIdentified;

@Getter
@AllArgsConstructor
public class IdentifiedRunnable implements UniqueIdentified {
    private String id;
    private Runnable runnable;
}
