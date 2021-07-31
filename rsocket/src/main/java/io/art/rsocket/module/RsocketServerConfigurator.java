package io.art.rsocket.module;

import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.server.method.*;
import io.art.server.registrator.*;
import lombok.experimental.*;
import static io.art.rsocket.module.RsocketModule.*;

@Accessors(fluent = true)
public class RsocketServerConfigurator extends ServerConfigurator {
    private boolean logging;

    public RsocketServerConfigurator logging() {
        return logging(true);
    }

    public RsocketServerConfigurator logging(boolean logging) {
        this.logging = logging;
        return this;
    }

    boolean hasLogging() {
        return logging;
    }

    ImmutableArray<LazyProperty<ServiceMethod>> serviceMethodProviders() {
        return get();
    }

    public RsocketServerConfigurator() {
        super(() -> rsocketModule().configuration().getServerConfiguration());
    }
}
