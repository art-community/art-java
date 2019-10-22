package ru.art.information.provider;

import lombok.experimental.*;
import ru.art.information.model.*;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.HttpServer.*;
import static ru.art.http.server.function.HttpServiceFunction.*;
import static ru.art.information.collector.GrpcInformationCollector.*;
import static ru.art.information.collector.HttpInformationCollector.*;
import static ru.art.information.mapping.InformationResponseMapper.*;
import static ru.art.information.mapping.TestModelMapper.*;

@UtilityClass
public class InformationProivder {
    public static void main(String[] args) {
        useAgileConfigurations();
        httpPost("/service")
                .requestMapper(toTestModel)
                .responseMapper(fromTestModel)
                .produce(() -> "test");
        httpGet("/information")
                .producesMimeType(applicationJsonUtf8())
                .consumesMimeType(applicationJsonUtf8())
                .ignoreRequestContentType()
                .ignoreRequestAcceptType()
                .responseMapper(fromInformationResponse)
                .produce(() -> InformationResponse.builder()
                        .httpInformation(collectHttpInformation())
                        .grpcInformation(collectGrpcInformation())
                        .build());
        startHttpServer().await();
    }
}
