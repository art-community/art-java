package ru.art.information.collector;

import lombok.experimental.*;
import ru.art.information.generator.*;
import ru.art.information.model.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.network.provider.IpAddressProvider.*;
import static ru.art.http.constants.HttpCommonConstants.*;
import static ru.art.http.server.module.HttpServerModule.*;

@UtilityClass
public class HttpInformationCollector {
    public static HttpInformation collectHttpInformation() {
        return HttpInformation.builder().services(httpServices()
                .stream()
                .map(service -> HttpServiceInformation
                        .builder()
                        .id(service.getServiceId())
                        .methods(service.getHttpService()
                                .getHttpMethods()
                                .stream()
                                .map(method -> HttpServiceMethodInformation
                                        .builder()
                                        .id(method.getMethodId())
                                        .url(HTTP_SCHEME
                                                + SCHEME_DELIMITER
                                                + getIpAddress()
                                                + COLON
                                                + httpServerModule().getPort()
                                                + service.getHttpService().getPath()
                                                + method.getPath())
                                        .exampleRequest(doIfNotNull(method.getRequestMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                        .exampleResponse(doIfNotNull(method.getResponseMapper(), ExampleJsonGenerator::generateExampleJson, () -> EMPTY_STRING))
                                        .build())
                                .collect(toMap(HttpServiceMethodInformation::getId, identity()))
                        )
                        .build())
                .collect(toMap(HttpServiceInformation::getId, identity())))
                .build();
    }
}
