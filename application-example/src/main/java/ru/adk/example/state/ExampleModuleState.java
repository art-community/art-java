package ru.adk.example.state;

import ru.adk.core.module.ModuleState;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * State is needed for store some static constants,
 * which are needed in service
 * <p>
 * In this example state is number of incoming requests
 */

public class ExampleModuleState implements ModuleState {
    private final AtomicInteger requests = new AtomicInteger();

    public int getRequests() {
        return requests.get();
    }

    public ExampleModuleState setRequests(int requests) {
        this.requests.set(requests);
        return this;
    }

    public int incrementRequests() {
        return requests.incrementAndGet();
    }
}
