package ru.adk.config.remote.service;

import static ru.adk.core.context.Context.context;

public interface RemoteConfigService {
    static void applyConfiguration() {
        context().refreshModules();
    }
}
