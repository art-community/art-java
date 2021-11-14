package io.art.logging.netty;

import io.art.logging.graal.*;
import io.netty.util.internal.logging.*;
import lombok.experimental.*;
import static io.art.logging.Logging.*;
import static io.art.logging.graal.GraalNettyLoggerFactory.*;

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

}
