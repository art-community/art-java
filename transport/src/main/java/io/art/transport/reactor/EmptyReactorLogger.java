package io.art.transport.reactor;

import reactor.util.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;

@SuppressWarnings(ALL)
public class EmptyReactorLogger implements Logger {
    public EmptyReactorLogger(String s) {

    }

    @Override
    public String getName() {
        return EmptyReactorLogger.class.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String message) {

    }

    @Override
    public void trace(String format, Object... arguments) {

    }

    @Override
    public void trace(String message, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void debug(String format, Object... arguments) {

    }

    @Override
    public void debug(String message, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String message) {

    }

    @Override
    public void info(String format, Object... arguments) {

    }

    @Override
    public void info(String message, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void warn(String format, Object... arguments) {

    }

    @Override
    public void warn(String message, Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String message) {

    }

    @Override
    public void error(String format, Object... arguments) {

    }

    @Override
    public void error(String message, Throwable throwable) {

    }
}
