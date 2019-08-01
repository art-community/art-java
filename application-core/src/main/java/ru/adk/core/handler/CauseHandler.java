package ru.adk.core.handler;

import lombok.RequiredArgsConstructor;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class CauseHandler {
    private final Exception outer;
    private Object result;

    public static CauseHandler handleCause(Exception outer) {
        return new CauseHandler(outer);
    }

    public <C> CauseHandler handle(Class<C> causeClass, Function<C, Object> handler) {
        Throwable cause = outer.getCause();
        if (isNotEmpty(cause) && cause.getClass().equals(causeClass)) {
            result = handler.apply(cast(cause));
        }
        return this;
    }

    public <C> CauseHandler consume(Class<C> causeClass, Consumer<C> handler) {
        Throwable cause = outer.getCause();
        if (isNotEmpty(cause) && cause.getClass().equals(causeClass)) {
            handler.accept(cast(cause));
        }
        return this;
    }

    public <T> T getResult() {
        return cast(result);
    }

    public <T> T getResult(T defaultResult) {
        return isEmpty(result) ? defaultResult : cast(result);
    }
}
