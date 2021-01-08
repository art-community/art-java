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
    public Entity fromThrowable(Throwable throwable) {
        EntityBuilder builder = entityBuilder()
                .lazyPut(stringPrimitive(CLASS_KEY), () -> stringPrimitive(throwable.getClass().getName()))
                .lazyPut(stringPrimitive(MESSAGE_KEY), () -> stringPrimitive(throwable.getMessage()))
                .lazyPut(stringPrimitive(STACK_TRACE_KEY), throwable::getStackTrace, fromArray(ThrowableMapping::fromStackTraceElement));
        apply(throwable.getCause(), cause -> builder.lazyPut(stringPrimitive(CAUSE_KEY), () -> fromThrowable(cause)));
        return builder.build();
    }

    public Entity fromThrowableNested(Throwable throwable) {
        return entityBuilder().lazyPut(stringPrimitive(EXCEPTION_KEY), () -> fromThrowable(throwable)).build();
    }


    public Throwable toThrowable(Entity entity) {
        String message = entity.map(stringPrimitive(MESSAGE_KEY), toString);
        StackTraceElement[] stackTrace = entity.map(stringPrimitive(STACK_TRACE_KEY), toArray(StackTraceElement[]::new, ThrowableMapping::toStackTraceElement));
        Throwable cause = entity.map(stringPrimitive(CAUSE_KEY), ThrowableMapping::toThrowable);
        Throwable throwable = new Throwable(message, cause);
        throwable.setStackTrace(stackTrace);
        return throwable;
    }

    public Throwable toThrowableNested(Entity entity) {
        return entity.mapping().map(EXCEPTION_KEY, ThrowableMapping::toThrowable);
    }


    public Value fromStackTraceElement(StackTraceElement element) {
        return Entity.entityBuilder()
                .put(stringPrimitive(DECLARING_CLASS_KEY), stringPrimitive(element.getClassName()))
                .put(stringPrimitive(METHOD_NAME_KEY), stringPrimitive(element.getMethodName()))
                .put(stringPrimitive(FILE_NAME_KEY), stringPrimitive(element.getFileName()))
                .put(stringPrimitive(LINE_NUMBER_KEY), intPrimitive(element.getLineNumber()))
                .build();
    }

    public StackTraceElement toStackTraceElement(Value value) {
        if (!Value.isEntity(value)) return null;
        Entity entity = Value.asEntity(value);
        String declaringClass = entity.map(stringPrimitive(DECLARING_CLASS_KEY), toString);
        String methodName = entity.map(stringPrimitive(METHOD_NAME_KEY), toString);
        String fileName = entity.map(stringPrimitive(FILE_NAME_KEY), toString);
        int lineNumber = entity.mapOrDefault(stringPrimitive(LINE_NUMBER_KEY), INT, toInt);
        return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
    }
}
