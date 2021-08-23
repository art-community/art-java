package io.art.logging.graal;

import io.netty.util.internal.logging.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class GraalNettyLoggerFactory extends InternalLoggerFactory {
    private final Map<String, GraalNettyLogger> loggers = map();

    public static final GraalNettyLoggerFactory GRAAL_NETTY_LOGGER_FACTORY = new GraalNettyLoggerFactory();

    @Override
    public InternalLogger newInstance(String name) {
        return putIfAbsent(loggers, name, () -> new GraalNettyLogger(name));
    }

    public void dispose() {
        for (GraalNettyLogger logger : loggers.values()) {
            logger.dispose();
        }
    }
}
