package io.art.model.configurator;

import io.netty.handler.codec.http.*;
import lombok.*;

import java.util.*;
import java.util.function.*;

import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.http.module.HttpModule.*;

@RequiredArgsConstructor
public class HttpServiceExceptionMappingConfigurator{
    private final Map<Class<? extends Throwable>, Function<? extends Throwable, ?>> mappers = mapOf(Throwable.class, exception -> {
        httpContext().status(500);
        return null;
    });

    public HttpServiceExceptionMappingConfigurator on(Class<? extends Throwable> exceptionClass, Function<? extends Throwable, ?> mapper){
        mappers.put(exceptionClass, mapper);
        return this;
    }

    public HttpServiceExceptionMappingConfigurator on(Class<? extends Throwable> exceptionClass, HttpResponseStatus httpStatus, Supplier<Object> responseSupplier){
        mappers.put(exceptionClass, exception -> {
            httpContext().status(httpStatus);
            return responseSupplier.get();
        });
        return this;
    }

    public HttpServiceExceptionMappingConfigurator on(Class<? extends Throwable> exceptionClass, HttpResponseStatus httpStatus){
        return on(exceptionClass, httpStatus, () -> null);
    }

    public HttpServiceExceptionMappingConfigurator on(Class<? extends Throwable> exceptionClass, Integer httpStatus, Supplier<Object> responseSupplier){
        return on(exceptionClass, HttpResponseStatus.valueOf(httpStatus), responseSupplier);
    }

    public HttpServiceExceptionMappingConfigurator on(Class<? extends Throwable> exceptionClass, Integer httpStatus){
        return on(exceptionClass, httpStatus, () -> null);
    }

    public Function<? extends Throwable, ?> configure(){
        return exception -> mappers.containsKey(exception.getClass()) ?
                mappers.get(exception.getClass()).apply(cast(exception)) :
                mappers.get(Throwable.class).apply(cast(exception));
    }

}
