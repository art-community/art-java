package io.art.logging.netty;

import io.netty.util.internal.logging.*;
import lombok.experimental.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.logging.Logging.*;
import static io.art.logging.netty.NettyLoggerFactory.GraalNettyLoggerFactory.*;
import java.util.*;

@UtilityClass
public class NettyLoggerFactory {
    private static final InternalLoggerFactory DEFAULT_NETTY_LOGGER_FACTORY = new InternalLoggerFactory() {
        @Override
        protected InternalLogger newInstance(String name) {
            return new NettyLogger(logger(name));
        }
    };

    public static InternalLoggerFactory defaultNettyLoggerFactory() {
        return DEFAULT_NETTY_LOGGER_FACTORY;
    }

    public static GraalNettyLoggerFactory graalNettyLoggerFactory() {
        return GRAAL_NETTY_LOGGER_FACTORY;
    }

    public static class GraalNettyLoggerFactory extends InternalLoggerFactory {
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
}
