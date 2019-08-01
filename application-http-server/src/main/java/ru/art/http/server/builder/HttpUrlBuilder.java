package ru.art.http.server.builder;

import lombok.Builder;
import lombok.Getter;
import static ru.art.core.constants.CharConstants.COLON;
import static ru.art.core.constants.StringConstants.SCHEME_DELIMITER;
import static ru.art.http.constants.HttpCommonConstants.DEFAULT_HTTP_PORT;

public interface HttpUrlBuilder {
    static String buildUrl(UrlInfo info) {
        StringBuilder url = new StringBuilder();
        int port = info.port;
        if (port < 0) {
            // Work around java.net.URL bug
            port = DEFAULT_HTTP_PORT;
        }

        String scheme = info.scheme;
        url.append(scheme);
        url.append(SCHEME_DELIMITER);
        url.append(info.serverName);
        url.append(COLON);
        url.append(port);
        url.append(info.uri);
        return url.toString();
    }

    @Getter
    @Builder
    class UrlInfo {
        private int port;
        private String scheme;
        private String serverName;
        private String uri;
    }
}
