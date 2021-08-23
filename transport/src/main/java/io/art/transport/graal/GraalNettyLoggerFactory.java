package io.art.transport.graal;

import io.art.core.property.*;
import io.art.logging.*;
import io.art.logging.netty.*;
import io.netty.util.internal.logging.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.DisposableProperty.*;
import static java.text.MessageFormat.*;
import static org.graalvm.nativeimage.ImageInfo.*;
import java.util.*;

public class GraalNettyLoggerFactory extends InternalLoggerFactory {
    private final Map<String, SubstitutedNettyLogger> loggers = map();

    @Override
    public InternalLogger newInstance(String name) {
        return putIfAbsent(loggers, name, () -> new SubstitutedNettyLogger(name));
    }

    public void dispose() {
        for (SubstitutedNettyLogger logger : loggers.values()) {
            logger.dispose();
        }
    }

    private static class SubstitutedNettyLogger extends AbstractInternalLogger {
        private final DisposableProperty<InternalLogger> logger;

        private SubstitutedNettyLogger(String name) {
            super(name);
            logger = disposable(() -> inImageBuildtimeCode()
                    ? ((JdkLoggerFactory) JdkLoggerFactory.INSTANCE).newInstance(name)
                    : new NettyLogger(Logging.logger(name)));
        }

        @Override
        public boolean isTraceEnabled() {
            return logger.get().isTraceEnabled();
        }

        @Override
        public void trace(String message) {
            logger.get().trace(message);
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
            logger.get().trace(format(format, arguments));
        }

        @Override
        public void trace(String message, Throwable throwable) {
            logger.get().trace(message);
        }

        @Override
        public boolean isDebugEnabled() {
            return logger.get().isDebugEnabled();
        }

        @Override
        public void debug(String message) {
            logger.get().debug(message);
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
            logger.get().debug(format(format, arguments));
        }

        @Override
        public void debug(String message, Throwable throwable) {
            logger.get().debug(message, throwable);
        }

        @Override
        public boolean isInfoEnabled() {
            return logger.get().isInfoEnabled();
        }

        @Override
        public void info(String message) {
            logger.get().info(message);
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
            logger.get().info(format(format, arguments));
        }

        @Override
        public void info(String message, Throwable throwable) {
            logger.get().info(message, throwable);
        }

        @Override
        public boolean isWarnEnabled() {
            return logger.get().isWarnEnabled();
        }

        @Override
        public void warn(String message) {
            logger.get().warn(message);
        }

        @Override
        public void warn(String format, Object argument) {
            warn(format(format, argument));
        }

        @Override
        public void warn(String format, Object... arguments) {
            logger.get().warn(format(format, arguments));
        }

        @Override
        public void warn(String format, Object firstArgument, Object secondArgument) {
            warn(format(format, firstArgument, firstArgument));
        }

        @Override
        public void warn(String message, Throwable throwable) {
            logger.get().warn(message, throwable);
        }

        @Override
        public boolean isErrorEnabled() {
            return logger.get().isErrorEnabled();
        }

        @Override
        public void error(String message) {
            logger.get().error(message);
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
            logger.get().error(format(format, arguments));
        }

        @Override
        public void error(String message, Throwable throwable) {
            logger.get().error(message, throwable);
        }

        public void dispose() {
            logger.dispose();
        }
    }
}
