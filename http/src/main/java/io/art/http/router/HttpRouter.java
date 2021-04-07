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

package io.art.http.router;

import io.art.core.mime.*;
import io.art.core.model.*;
import io.art.http.configuration.*;
import io.art.http.exception.*;
import io.art.http.model.*;
import io.art.http.state.*;
import io.art.server.specification.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.immutable.*;
import io.netty.buffer.*;
import io.netty.handler.codec.http.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import reactor.netty.http.server.*;
import reactor.netty.http.websocket.*;

import java.util.*;

import static io.art.core.caster.Caster.*;
import static io.art.core.mime.MimeTypes.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.http.constants.HttpModuleConstants.ExceptionMessages.*;
import static io.art.http.constants.HttpModuleConstants.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.payload.HttpPayloadReader.*;
import static io.art.http.payload.HttpPayloadWriter.*;
import static io.art.server.module.ServerModule.*;
import static io.art.value.constants.ValueModuleConstants.DataFormat.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static java.text.MessageFormat.*;

public class HttpRouter {

    public HttpRouter(HttpServerRoutes routes, HttpServerConfiguration configuration) {
        for (Map.Entry<String, HttpServiceConfiguration> service : configuration.getServices().entrySet()) {
            for (Map.Entry<String, HttpMethodConfiguration> method : service.getValue().getMethods().entrySet()) {
                HttpMethodConfiguration methodValue = method.getValue();
                HttpMethodType httpMethodType = methodValue.getMethod();
                switch (httpMethodType){
                    case GET:
                        routes.get(methodValue.getPath(), (request, response) -> handleHttp(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case POST:
                        routes.post(methodValue.getPath(), (request, response) -> handleHttp(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case PUT:
                        routes.put(methodValue.getPath(), (request, response) -> handleHttp(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case DELETE:
                        routes.delete(methodValue.getPath(), (request, response) -> handleHttp(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case OPTIONS:
                        routes.options(methodValue.getPath(), (request, response) -> handleHttp(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case HEAD:
                        routes.head(methodValue.getPath(), (request, response) -> handleHttp(findSpecification(serviceMethod(service.getKey(), method.getKey())), request, response));
                        break;
                    case WEBSOCKET:
                        routes.ws(methodValue.getPath(), (inbound, outbound) -> handleWebsocket(findSpecification(serviceMethod(service.getKey(), method.getKey())), inbound, outbound));
                }
            }
        }
    }

    private Publisher<Void> handleWebsocket(ServiceMethodSpecification specification, WebsocketInbound inbound, WebsocketOutbound outbound) {
        DataFormat dataFormat = findConfiguration(specification).getDefaultDataFormat();
        return inbound.receive()
                .map(content -> readPayloadData(dataFormat, content))
                .map(HttpPayloadValue::getValue)

//                .map(item -> specification.getInputMapper().map(cast(item)))
//                .transform(flux -> specification.decorateInput(cast(flux)))
//                .map(item -> specification.getImplementation().serve(item))
//                .transform(flux -> specification.decorateOutput(cast(flux)))
//                .map(item -> specification.getOutputMapper().map(cast(item)))

                .transform(specification::serve)

                .map(output -> writePayloadData(dataFormat, output))
                .transform(outbound::send);
    }

    private Publisher<Void> handleHttp(ServiceMethodSpecification specification, HttpServerRequest request, HttpServerResponse response) {
        HttpHeaders headers = request.requestHeaders();
        String contentType = headers.get(CONTENT_TYPE);
        String acceptType = headers.get(ACCEPT);
        DataFormat defaultDataFormat = findConfiguration(specification).getDefaultDataFormat();
        DataFormat inputDataFormat = ignoreException(() -> fromMimeType(MimeType.valueOf(contentType)), ignored -> defaultDataFormat);
        DataFormat outputDataFormat = ignoreException(() -> fromMimeType(MimeType.valueOf(acceptType)), ignored -> defaultDataFormat);


        Sinks.Many<ByteBuf> unicast = Sinks.many().unicast().onBackpressureBuffer();

        return request.receive()
                .doFirst(() -> httpContextFrom(request, response))
                .map(content -> readPayloadData(inputDataFormat, content))
                .map(HttpPayloadValue::getValue)
                .transform(specification::serve)
                .onErrorResume(throwable -> mapException(specification, throwable))
                .map(output -> writePayloadData(outputDataFormat, output))
                .contextWrite(ctx -> ctx.put(HttpContext.class, HttpContext.from(request, response)))
                .doOnNext(unicast::tryEmitNext)

                .doOnComplete(unicast::tryEmitComplete)
                .thenMany(response.send(unicast.asFlux()));
    }

    private ServiceMethodSpecification findSpecification(ServiceMethodIdentifier serviceMethodId) {
        return specifications()
                .findMethodById(serviceMethodId)
                .orElseThrow(() -> new HttpException(format(SPECIFICATION_NOT_FOUND, serviceMethodId)));
    }

    private HttpMethodConfiguration findConfiguration(ServiceMethodSpecification specification){
        return httpModule().configuration().getServerConfiguration().getServices().get(specification.getServiceId())
                .getMethods().get(specification.getMethodId());
    }

    private Flux<Value> mapException(ServiceMethodSpecification specification, Throwable exception){
        Object result = httpModule().configuration().getServerConfiguration().getServices().get(specification.getServiceId())
                .getExceptionMapper()
                .apply(cast(exception));
        return Flux.just(specification.getOutputMapper().map(cast(result)));
    }
}
