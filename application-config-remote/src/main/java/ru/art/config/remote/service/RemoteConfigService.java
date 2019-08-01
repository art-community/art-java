package ru.art.config.remote.service;

import static ru.art.core.context.Context.context;

public interface RemoteConfigService {
    static void applyConfiguration() {
        context().refreshModules();
    }
}
