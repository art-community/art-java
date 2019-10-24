package ru.art.information.specification;

import lombok.experimental.*;
import ru.art.http.server.HttpServerModuleConfiguration.HttpResourceConfiguration.*;
import ru.art.http.server.specification.*;
import ru.art.information.service.*;
import static ru.art.core.context.Context.*;
import static ru.art.http.constants.HttpCommonConstants.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.HttpResourceType.*;
import static ru.art.http.server.function.HttpServiceFunction.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.information.constants.InformationModuleConstants.*;
import static ru.art.information.mapping.InformationResponseMapper.*;
import static ru.art.information.mapping.StatusResponseMapper.*;
import static ru.art.service.ServiceModule.*;

@UtilityClass
public class InformationServiceSpecification {
    public static void registerInformationService(String modulePath) {
        serviceModuleState().getServiceRegistry().registerService(new HttpResourceServiceSpecification(modulePath + INFORMATION_PATH + WEB_UI_PATH, httpServerModule()
                .getResourceConfiguration()
                .toBuilder()
                .templateResourceVariable(WEB_UI_PATH_VARIABLE, modulePath + INFORMATION_PATH + WEB_UI_PATH)
                .templateResourceVariable(MAIN_MODULE_ID_VARIABLE, contextConfiguration().getMainModuleId())
                .defaultResource(new HttpResource(INFORMATION_INDEX_HTML, STRING, contextConfiguration().getCharset()))
                .build()));
        httpGet(modulePath + GET_INFORMATION_PATH)
                .producesMimeType(applicationJsonUtf8())
                .consumesMimeType(applicationJsonUtf8())
                .ignoreRequestContentType()
                .ignoreRequestAcceptType()
                .responseMapper(fromInformationResponse)
                .produce(InformationService::getInformation);
        httpGet(modulePath + STATUS_PATH)
                .producesMimeType(applicationJsonUtf8())
                .consumesMimeType(applicationJsonUtf8())
                .ignoreRequestContentType()
                .ignoreRequestAcceptType()
                .responseMapper(fromStatusResponse)
                .produce(InformationService::getStatus);
    }
}
