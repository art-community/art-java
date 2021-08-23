package io.art.logging.netty;

import io.art.logging.*;
import io.art.logging.logger.*;
import io.netty.util.internal.logging.*;
import reactor.util.annotation.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static java.lang.String.*;
import static java.util.Objects.*;
import static java.util.regex.Matcher.*;

public class NettyLogger extends AbstractInternalLogger {
    public final static InternalLoggerFactory NETTY_LOGGER_FACTORY = new InternalLoggerFactory() {
        @Override
        protected InternalLogger newInstance(String name) {
            return new NettyLogger(Logging.logger(name));
        }
    };

    private final Logger logger;

    public NettyLogger(Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String message) {
        logger.trace(message);
    }

    @Override
    public void trace(String format, Object argument) {
        trace(format(format, argument));
    }

    @Override
    public void trace(String format, Object firstArgument, Object secondArgument) {
        trace(format(format, firstArgument, secondArgument));
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format(format, arguments));
    }

    @Override
    public void trace(String message, Throwable throwable) {
        logger.trace(message);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(String format, Object argument) {
        debug(format(format, argument));
    }

    @Override
    public void debug(String format, Object firstArgument, Object secondArgument) {
        debug(format(format, firstArgument, secondArgument));
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format(format, arguments));
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logger.debug(message, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String format, Object argument) {
        info(format(format, argument));
    }

    @Override
    public void info(String format, Object firstArgument, Object secondArgument) {
        info(format(format, firstArgument, secondArgument));
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format(format, arguments));
    }

    @Override
    public void info(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(String format, Object argument) {
        warn(format(format, argument));
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format(format, arguments));
    }

    @Override
    public void warn(String format, Object firstArgument, Object secondArgument) {
        warn(format(format, firstArgument, firstArgument));
    }

    @Override
    public void warn(String message, Throwable throwable) {
        logger.warn(message, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String format, Object argument) {
        error(format(format, argument));
    }

    @Override
    public void error(String format, Object firstArgument, Object secondArgument) {
        error(format(format, firstArgument, secondArgument));
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format(format, arguments));
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    private String format(@Nullable String from, @Nullable Object... arguments) {
        if (isNull(from) || isEmpty(from)) {
            return EMPTY_STRING;
        }
        String computed = from;
        if (nonNull(arguments) && isNotEmpty(arguments)) {
            for (Object argument : arguments) {
                computed = computed.replaceFirst(FORMAT_REGEX, quoteReplacement(valueOf(argument)));
            }
        }
        return computed;
    }
}
