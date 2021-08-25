package io.art.http.module;

import io.art.core.annotation.*;
import io.art.core.property.*;
import io.art.http.configuration.*;
import io.art.http.configuration.HttpServerConfiguration.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Public
public class HttpServerConfigurator extends ServerConfigurator<HttpServerConfigurator> {
    private UnaryOperator<HttpServerConfigurationBuilder> configurator = identity();

    public HttpServerConfigurator configure(UnaryOperator<HttpServerConfigurationBuilder> configurator) {
        this.configurator = configurator;
        return this;
    }

    HttpServerConfiguration configure(HttpServerConfiguration current) {
        return configurator.apply(current.toBuilder()).build();
    }

    ServerConfiguration configureServer(LazyProperty<ServerConfiguration> configurationProvider, ServerConfiguration current) {
        return configure(configurationProvider, current);
    }
}
