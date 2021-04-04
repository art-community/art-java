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
    private final Map<Class<? extends Throwable>, Function<? extends Throwable, ?>> mappers = map();
    private final Class<?> serviceClass;

    public HttpServiceExceptionMappingConfigurator mapException(Class<? extends Throwable> exceptionClass, Function<? extends Throwable, ?> mapper){
        mappers.put(exceptionClass, mapper);
        return this;
    }

    public HttpServiceExceptionMappingConfigurator mapException(Class<? extends Throwable> exceptionClass, HttpResponseStatus httpStatus, Supplier<Object> responseSupplier){
        mappers.put(exceptionClass, exception -> {
            httpContext().status(httpStatus);
            return responseSupplier.get();
        });
        return this;
    }

    public HttpServiceExceptionMappingConfigurator mapException(Class<? extends Throwable> exceptionClass, HttpResponseStatus httpStatus){
        return mapException(exceptionClass, httpStatus, () -> null);
    }

    public HttpServiceExceptionMappingConfigurator mapException(Class<? extends Throwable> exceptionClass, Integer httpStatus, Supplier<Object> responseSupplier){
        return mapException(exceptionClass, HttpResponseStatus.valueOf(httpStatus), responseSupplier);
    }

    public HttpServiceExceptionMappingConfigurator mapException(Class<? extends Throwable> exceptionClass, Integer httpStatus){
        return mapException(exceptionClass, httpStatus, () -> null);
    }

    public Function<? extends Throwable, ?> configure(){
        return exception -> mappers.get(exception.getClass()).apply(cast(exception));
    }

}
