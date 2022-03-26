package io.art.core.context;

import io.art.core.module.Module;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import java.util.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class ManagedModule {
    private final Module<?, ?> module;
    private final List<Consumer<ContextService>> onLoad = linkedList();
    private final List<Consumer<ContextService>> onUnload = linkedList();
    private final List<Consumer<ContextService>> onBeforeReload = linkedList();
    private final List<Consumer<ContextService>> onAfterReload = linkedList();
    private final List<Consumer<ContextService>> onLaunch = linkedList();
    private final List<Consumer<ContextService>> onShutdown = linkedList();

    public void onLoad(Consumer<ContextService> consumer) {
        onLoad.add(consumer);
    }

    public void onUnload(Consumer<ContextService> consumer) {
        onUnload.add(consumer);
    }

    public void onBeforeReload(Consumer<ContextService> consumer) {
        onBeforeReload.add(consumer);
    }

    public void onAfterReload(Consumer<ContextService> consumer) {
        onAfterReload.add(consumer);
    }

    public void onLaunch(Consumer<ContextService> consumer) {
        onLaunch.add(consumer);
    }

    public void onShutdown(Consumer<ContextService> consumer) {
        onShutdown.add(consumer);
    }

    public void onLoad(ContextService service) {
        onLoad.forEach(consumer -> consumer.accept(service));
    }

    public void onUnload(ContextService service) {
        onUnload.forEach(consumer -> consumer.accept(service));
    }

    public void onBeforeReload(ContextService service) {
        onBeforeReload.forEach(consumer -> consumer.accept(service));
    }

    public void onAfterReload(ContextService service) {
        onAfterReload.forEach(consumer -> consumer.accept(service));
    }

    public void onLaunch(ContextService service) {
        onLaunch.forEach(consumer -> consumer.accept(service));
    }

    public void onShutdown(ContextService service) {
        onShutdown.forEach(consumer -> consumer.accept(service));
    }
}
