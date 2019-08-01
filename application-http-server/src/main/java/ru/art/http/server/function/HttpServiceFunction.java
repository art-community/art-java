package ru.art.http.server.function;

import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.server.builder.HttpMethodBuilderImplementation;
import ru.art.http.server.interceptor.HttpServerInterceptor;
import ru.art.service.constants.RequestValidationPolicy;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.http.server.constants.HttpServerModuleConstants.EXECUTE_HTTP_FUNCTION;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class HttpServiceFunction {
    private String path;
    private HttpMethodBuilderImplementation httpMethodBuilder;

    public HttpServiceFunction fromBody() {
        httpMethodBuilder.fromBody();
        return this;
    }

    public HttpServiceFunction addReqInterceptor(HttpServerInterceptor interceptor) {
        httpMethodBuilder.addRequestInterceptor(interceptor);
        return this;
    }

    public HttpServiceFunction addRespInterceptor(HttpServerInterceptor interceptor) {
        httpMethodBuilder.addResponseInterceptor(interceptor);
        return this;
    }

    public HttpServiceFunction responseMapper(ValueFromModelMapper responseMapper) {
        httpMethodBuilder.responseMapper(responseMapper);
        return this;
    }

    public HttpServiceFunction requestMapper(ValueToModelMapper requestMapper) {
        httpMethodBuilder.requestMapper(requestMapper);
        return this;
    }

    public HttpServiceFunction exceptionMapper(ValueFromModelMapper exceptionMapper) {
        httpMethodBuilder.exceptionMapper(exceptionMapper);
        return this;
    }

    public HttpServiceFunction fromQueryParameters() {
        httpMethodBuilder.fromQueryParameters();
        return this;
    }

    public HttpServiceFunction fromPathParameters(String... parameters) {
        httpMethodBuilder.fromPathParameters(parameters);
        return this;
    }

    public HttpServiceFunction consumesMimeType(MimeToContentTypeMapper mimeType) {
        httpMethodBuilder.consumes(mimeType);
        return this;
    }

    public HttpServiceFunction ignoreRequestContentType() {
        httpMethodBuilder.ignoreRequestContentType();
        return this;
    }

    public HttpServiceFunction validationPolicy(RequestValidationPolicy policy) {
        httpMethodBuilder.validationPolicy(policy);
        return this;
    }

    public HttpServiceFunction producesMimeType(MimeToContentTypeMapper mimeType) {
        httpMethodBuilder.produces(mimeType);
        return this;
    }

    public HttpServiceFunction ignoreRequestAcceptType() {
        httpMethodBuilder.ignoreRequestAcceptType();
        return this;
    }

    public HttpServiceFunction overrideResponseContentType() {
        httpMethodBuilder.overrideResponseContentType();
        return this;
    }

    public void handle(Function<?, ?> function) {
        serviceModule()
                .getServiceRegistry()
                .registerService(new HttpFunctionalServiceSpecification(httpMethodBuilder.listen(path).serve(EMPTY_STRING), function));
    }

    public void consume(Consumer<?> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public void produce(Supplier<?> producer) {
        handle(request -> producer.get());
    }

    public static HttpServiceFunction httpGet(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().get(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpPost(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().post(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpPut(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().put(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpPatch(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().patch(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpDelete(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().delete(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpTrace(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().trace(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpConnect(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().connect(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }

    public static HttpServiceFunction httpOptions(String path) {
        HttpServiceFunction httpServiceFunction = new HttpServiceFunction();
        httpServiceFunction.path = path;
        httpServiceFunction.httpMethodBuilder = (HttpMethodBuilderImplementation) httpService().options(EXECUTE_HTTP_FUNCTION);
        return httpServiceFunction;
    }
}
