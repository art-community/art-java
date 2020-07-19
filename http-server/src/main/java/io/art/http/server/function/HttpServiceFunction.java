/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.http.server.function;

import io.art.entity.immutable.*;
import io.art.entity.interceptor.*;
import io.art.entity.mapper.*;
import io.art.http.constants.*;
import io.art.http.server.builder.*;
import io.art.http.server.interceptor.*;
import io.art.server.constants.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import static io.art.http.server.model.HttpService.*;
import static io.art.server.module.ServerModule.*;
import java.util.function.*;

public class HttpServiceFunction {
    private String path;
    private HttpMethodBuilderImplementation httpMethodBuilder;

    public HttpServiceFunction fromBody() {
        httpMethodBuilder.fromBody();
        return this;
    }

    public HttpServiceFunction addRequestInterceptor(HttpServerInterceptor interceptor) {
        httpMethodBuilder.addRequestInterceptor(interceptor);
        return this;
    }

    public HttpServiceFunction addResponseInterceptor(HttpServerInterceptor interceptor) {
        httpMethodBuilder.addResponseInterceptor(interceptor);
        return this;
    }

    public <ResponseType> HttpServiceFunction responseMapper(ValueFromModelMapper<ResponseType, ? extends Value> responseMapper) {
        httpMethodBuilder.responseMapper(responseMapper);
        return this;
    }

    public <RequestType> HttpServiceFunction requestMapper(ValueToModelMapper<RequestType, ? extends Value> requestMapper) {
        httpMethodBuilder.requestMapper(requestMapper);
        return this;
    }

    public HttpServiceFunction exceptionMapper(ValueFromModelMapper<Throwable, ? extends Value> exceptionMapper) {
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

    public <RequestType, ResponseType> void handle(Function<RequestType, ResponseType> function) {
        serviceModuleState()
                    .getServiceRegistry()
                .register(new HttpFunctionalServiceSpecification(httpMethodBuilder.listen(path).serve(EMPTY_STRING), function));
    }

    public <RequestType> void consume(Consumer<RequestType> consumer) {
        handle(request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public <ResponseType> void produce(Supplier<ResponseType> producer) {
        handle(request -> producer.get());
    }


    public HttpServiceFunction addRequestValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        httpMethodBuilder.addRequestValueInterceptor(interceptor);
        return this;
    }

    public HttpServiceFunction addResponseValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        httpMethodBuilder.addResponseValueInterceptor(interceptor);
        return this;
    }

    public HttpServiceFunction exceptionValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        httpMethodBuilder.addExceptionValueInterceptor(interceptor);
        return this;
    }

    public HttpServiceFunction checkedResponse() {
        httpMethodBuilder.checkedResponse();
        return this;
    }

    public HttpServiceFunction uncheckedResponse() {
        httpMethodBuilder.uncheckedResponse();
        return this;
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
