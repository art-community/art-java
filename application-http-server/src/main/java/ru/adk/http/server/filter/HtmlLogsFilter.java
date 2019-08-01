package ru.adk.http.server.filter;

import org.zalando.logbook.RawHttpResponse;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.http.constants.HttpMimeTypes.*;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.WEB_RESOURCE;

public interface HtmlLogsFilter {
    static String replaceWebResponseBody(RawHttpResponse response) {
        if (TEXT_HTML.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (TEXT_JS.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (TEXT_CSS.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_WEBP.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_PNG.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_JPEG.toString().equals(response.getContentType())) return WEB_RESOURCE;
        if (IMAGE_GIF.toString().equals(response.getContentType())) return WEB_RESOURCE;
        return EMPTY_STRING;
    }
}
