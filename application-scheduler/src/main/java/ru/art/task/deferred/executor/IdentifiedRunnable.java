package ru.art.task.deferred.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.core.identified.UniqueIdentified;

@Getter
@AllArgsConstructor
public class IdentifiedRunnable implements UniqueIdentified {
    private String id;
    private Runnable runnable;
}
