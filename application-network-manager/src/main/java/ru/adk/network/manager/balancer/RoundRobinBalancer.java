package ru.adk.network.manager.balancer;

import lombok.Getter;
import ru.adk.state.api.model.ModuleEndpoint;
import static java.util.stream.Collectors.toMap;
import static ru.adk.core.factory.CollectionsFactory.concurrentHashMap;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RoundRobinBalancer implements Balancer {
    private final Map<String, List<ModuleEndpoint>> moduleEndpoints = concurrentHashMap();
    private final AtomicInteger position = new AtomicInteger(0);
    @Getter(lazy = true)
    private final int listSize = moduleEndpoints.size();

    @Override
    public ModuleEndpoint select(String modulePath) {
        return moduleEndpoints.get(modulePath).get(getNextPosition());
    }

    @Override
    public Collection<ModuleEndpoint> getEndpoints(String modulePath) {
        return moduleEndpoints.get(modulePath);
    }

    @Override
    public void updateEndpoints(Map<String, Collection<ModuleEndpoint>> moduleEndpoints) {
        this.moduleEndpoints.clear();
        this.moduleEndpoints.putAll(moduleEndpoints.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> fixedArrayOf(entry.getValue()))));
    }

    private int getNextPosition() {
        for (; ; ) {
            int current = position.get();
            int next = current + 1;
            if (next >= getListSize()) {
                next = 0;
            }
            if (position.compareAndSet(current, next))
                return current;
        }
    }

}
