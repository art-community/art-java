package ru.adk.http.server.extractor;


import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.StringConstants.DOT;
import static ru.adk.http.constants.HttpMimeTypes.*;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.ResourceExtensions.*;

public interface HttpWebResponseContentTypeExtractor {
    static String extractTypeByFile(String fileUrl) {
        if (isEmpty(fileUrl)) return ALL.toString();
        if (!fileUrl.contains(DOT)) return TEXT_HTML.toString();
        String ext = fileUrl.substring(fileUrl.indexOf(DOT));
        if (ext.contains(WEBP)) {
            return IMAGE_WEBP.toString();
        }
        if (ext.contains(JPEG)) {
            return IMAGE_JPEG.toString();
        }
        if (ext.contains(PNG)) {
            return IMAGE_PNG.toString();
        }
        if (ext.contains(CSS)) {
            return TEXT_CSS.toString();
        }
        if (ext.contains(MAP)) {
            return TEXT_JS.toString();
        }
        if (ext.contains(JS)) {
            return TEXT_JS.toString();
        }
        if (ext.contains(HTML)) {
            return TEXT_HTML.toString();
        }
        return ALL.toString();
    }
}
