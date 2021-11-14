package io.art.logging.graal;

import com.oracle.svm.core.annotate.*;
import io.art.core.property.*;
import io.art.logging.*;
import io.art.logging.netty.*;
import io.netty.util.internal.logging.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.property.DisposableProperty.*;
import static io.art.logging.netty.NettyLoggerFactory.*;
import static org.graalvm.nativeimage.ImageInfo.*;


@SuppressWarnings(UNUSED)
@TargetClass(value = InternalLoggerFactory.class)
final class TargetNettyInternalLoggerFactory {
    @Substitute
    private static InternalLoggerFactory newDefaultFactory(String name) {
        return graalNettyLoggerFactory();
    }
}

public class GraalNettyLogger extends AbstractInternalLogger {
    private final DisposableProperty<InternalLogger> logger;

    public GraalNettyLogger(String name) {
        super(name);
        logger = disposable(() -> inImageBuildtimeCode() || !withLogging()
                ? ((JdkLoggerFactory) JdkLoggerFactory.INSTANCE).newInstance(name)
                : new NettyLogger(Logging.logger(name)));
    }

    public void dispose() {
        logger.dispose();
    }

    @Override
    public String name() {
        return logger.get().name();
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
        logger.get().trace(format, argument);
    }

    @Override
    public void trace(String format, Object firstArgument, Object secondArgument) {
        logger.get().trace(format, firstArgument, secondArgument);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.get().trace(format, arguments);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        logger.get().trace(message, throwable);
    }

    @Override
    public void trace(Throwable throwable) {
        logger.get().trace(throwable);
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
        logger.get().debug(format, argument);
    }

    @Override
    public void debug(String format, Object firstArgument, Object secondArgument) {
        logger.get().debug(format, firstArgument, secondArgument);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.get().debug(format, arguments);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        logger.get().debug(message, throwable);
    }

    @Override
    public void debug(Throwable throwable) {
        logger.get().debug(throwable);
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
        logger.get().info(format, argument);
    }

    @Override
    public void info(String format, Object firstArgument, Object secondArgument) {
        logger.get().info(format, firstArgument, secondArgument);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.get().info(format, arguments);
    }

    @Override
    public void info(String message, Throwable throwable) {
        logger.get().info(message, throwable);
    }

    @Override
    public void info(Throwable throwable) {
        logger.get().info(throwable);
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
        logger.get().warn(format, argument);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.get().warn(format, arguments);
    }

    @Override
    public void warn(String format, Object firstArgument, Object secondArgument) {
        logger.get().warn(format, firstArgument, secondArgument);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        logger.get().warn(message, throwable);
    }

    @Override
    public void warn(Throwable throwable) {
        logger.get().warn(throwable);
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
        logger.get().error(format, argument);
    }

    @Override
    public void error(String format, Object firstArgument, Object secondArgument) {
        logger.get().error(format, firstArgument, secondArgument);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.get().error(format, arguments);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.get().error(message, throwable);
    }

    @Override
    public void error(Throwable throwable) {
        logger.get().error(throwable);
    }

    @Override
    public boolean isEnabled(InternalLogLevel level) {
        return logger.get().isEnabled(level);
    }

    @Override
    public void log(InternalLogLevel level, String message) {
        logger.get().log(level, message);
    }

    @Override
    public void log(InternalLogLevel level, String format, Object argument) {
        logger.get().log(level, format, argument);
    }

    @Override
    public void log(InternalLogLevel level, String format, Object firstArgument, Object secondArgument) {
        logger.get().log(level, format, firstArgument, secondArgument);
    }

    @Override
    public void log(InternalLogLevel level, String format, Object... arguments) {
        logger.get().log(level, format, arguments);
    }

    @Override
    public void log(InternalLogLevel level, String message, Throwable throwable) {
        logger.get().log(level, message, throwable);
    }

    @Override
    public void log(InternalLogLevel level, Throwable throwable) {
        logger.get().log(level, throwable);
    }
}
