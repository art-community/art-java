package io.art.server.configurator;

import io.art.server.configuration.*;

public class ServiceMethodConfigurator {
    private boolean logging = false;
    private boolean validating = true;
    private boolean deactivated = false;

    public ServiceMethodConfigurator logging() {
        return logging(true);
    }

    public ServiceMethodConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    public ServiceMethodConfigurator validating() {
        return validating(true);
    }

    public ServiceMethodConfigurator validating(boolean validating) {
        this.validating = validating;
        return this;
    }

    public ServiceMethodConfigurator deactivated() {
        return deactivated(true);
    }

    public ServiceMethodConfigurator deactivated(boolean deactivated) {
        this.deactivated = deactivated;
        return this;
    }

    ServiceMethodConfiguration configure(ServiceMethodConfiguration configuration) {
        return configuration.toBuilder()
                .deactivated(deactivated)
                .logging(logging)
                .validating(validating)
                .build();
    }
}
