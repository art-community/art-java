package io.art.core.module;

import lombok.*;
import static io.art.core.module.ModuleLauncher.Config.*;

public class ModuleLauncher {
    class Example implements Module<Config, Configurator, ModuleState> {
        @Override
        public Config getConfiguration() {
            return INSTANCE;
        }

        @Override
        public String getId() {
            return "example";
        }
    }


    static class Config implements ModuleConfiguration<Config> {
        private String value = "Test";

        @Override
        public Configurator configurator() {
            return new Configurator(this);
        }

        public static Config INSTANCE = new Config();
    }

    @RequiredArgsConstructor
    static class Configurator implements ModuleConfigurator<Config> {
        private final Config config;

        public Configurator value(String value) {
            config.value = value;
            return this;
        }

        @Override
        public Config configure() {
            return config;
        }
    }

    public void launch(ModuleModel model) {
        new Example().configure(configurator -> configurator.value("test"));
    }
}
