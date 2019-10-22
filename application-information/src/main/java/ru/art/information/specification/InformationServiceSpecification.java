package ru.art.information.specification;

import lombok.experimental.*;
import ru.art.information.service.*;
import static ru.art.entity.PrimitiveMapping.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.function.HttpServiceFunction.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.http.server.service.HttpResourceService.*;
import static ru.art.information.constants.InformationModuleConstants.*;
import static ru.art.information.mapping.InformationResponseMapper.*;

@UtilityClass
public class InformationServiceSpecification {
    public static void registerInformationService() {
        httpGet(httpServerModule().getPath() + INFORMATION_PATH)
                .responseMapper(stringMapper.getFromModel())
                .produce(() -> getStringResource(INFORMATION_INDEX_HTML));
        httpGet(httpServerModule().getPath() + INFORMATION_API_PATH)
                .producesMimeType(applicationJsonUtf8())
                .consumesMimeType(applicationJsonUtf8())
                .ignoreRequestContentType()
                .ignoreRequestAcceptType()
                .responseMapper(fromInformationResponse)
                .produce(InformationService::getInformation);
        httpGet(httpServerModule().getPath() + STATUS_PATH)
                .responseMapper(boolMapper.getFromModel())
                .produce(InformationService::getStatus);
    }
}
