package io.art.value.mapping;

import io.art.value.builder.*;
import io.art.value.immutable.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.value.constants.ValueModuleConstants.Fields.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.PrimitiveType.*;
import static io.art.value.factory.PrimitivesFactory.*;
import static io.art.value.immutable.Entity.*;
import static io.art.value.mapping.ArrayMapping.*;
import static io.art.value.mapping.PrimitiveMapping.*;

@UtilityClass
public class ThrowableMapping {
    private static final Primitive CLASS = stringPrimitive(CLASS_KEY);
    private static final Primitive MESSAGE = stringPrimitive(MESSAGE_KEY);
    private static final Primitive STACK_TRACE = stringPrimitive(STACK_TRACE_KEY);
    private static final Primitive EXCEPTION = stringPrimitive(EXCEPTION_KEY);
    private static final Primitive DECLARING_CLASS = stringPrimitive(DECLARING_CLASS_KEY);
    private static final Primitive METHOD_NAME = stringPrimitive(METHOD_NAME_KEY);
    private static final Primitive FILE_NAME = stringPrimitive(FILE_NAME_KEY);
    private static final Primitive LINE_NUMBER = stringPrimitive(LINE_NUMBER_KEY);
    private static final Primitive CAUSE = stringPrimitive(CAUSE_KEY);

    public Entity fromThrowable(Throwable throwable) {
        EntityBuilder builder = entityBuilder()
                .lazyPut(CLASS, () -> stringPrimitive(throwable.getClass().getName()))
                .lazyPut(MESSAGE, () -> stringPrimitive(throwable.getMessage()))
                .lazyPut(STACK_TRACE, throwable::getStackTrace, fromArray(ThrowableMapping::fromStackTraceElement));
        apply(throwable.getCause(), cause -> builder.lazyPut(CAUSE, () -> fromThrowable(cause)));
        return builder.build();
    }

    public Entity fromThrowableNested(Throwable throwable) {
        return entityBuilder().lazyPut(EXCEPTION, () -> fromThrowable(throwable)).build();
    }


    public Throwable toThrowable(Entity entity) {
        String message = entity.map(MESSAGE, toString);
        StackTraceElement[] stackTrace = entity.map(STACK_TRACE, toArrayRaw(StackTraceElement[]::new, ThrowableMapping::toStackTraceElement));
        Throwable cause = entity.map(CAUSE, ThrowableMapping::toThrowable);
        Throwable throwable = new Throwable(message, cause);
        throwable.setStackTrace(stackTrace);
        return throwable;
    }

    public Throwable toThrowableNested(Entity entity) {
        return entity.map(EXCEPTION, ThrowableMapping::toThrowable);
    }


    public Value fromStackTraceElement(StackTraceElement element) {
        return Entity.entityBuilder()
                .put(DECLARING_CLASS, stringPrimitive(element.getClassName()))
                .put(METHOD_NAME, stringPrimitive(element.getMethodName()))
                .put(FILE_NAME, stringPrimitive(element.getFileName()))
                .put(LINE_NUMBER, intPrimitive(element.getLineNumber()))
                .build();
    }

    public StackTraceElement toStackTraceElement(Value value) {
        if (!Value.isEntity(value)) return null;
        Entity entity = Value.asEntity(value);
        String declaringClass = entity.map(DECLARING_CLASS, toString);
        String methodName = entity.map(METHOD_NAME, toString);
        String fileName = entity.map(FILE_NAME, toString);
        int lineNumber = entity.mapOrDefault(LINE_NUMBER, INT, toInt);
        return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
    }
}
