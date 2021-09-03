package io.art.http.message;

import io.art.core.collection.*;
import io.art.http.configuration.*;
import lombok.experimental.*;
import static io.art.core.constants.ProtocolConstants.*;
import static io.art.core.constants.StringConstants.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.stream.*;

@UtilityClass
public class HttpMessageBuilder {
    public static String httpLaunchedMessage(HttpModuleConfiguration configuration) {
        StringBuilder message = new StringBuilder("HTTP module launched\n\t");
        if (configuration.isEnableServer()) {
            message.append("HTTP Server - ")
                    .append(HTTP_SCHEME + SCHEME_DELIMITER)
                    .append(configuration.getHttpServer().getHost())
                    .append(COLON)
                    .append(configuration.getHttpServer().getPort())
                    .append("\n\t");
        }
        message.append("Methods:\n\t\t").append(configuration.getServer()
                .getMethods()
                .get()
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + SPACE + COLON + SPACE + entry.getValue().getInvoker().getDelegate())
                .collect(joining("\n\t\t")));
        message.append("\n\t");
        ImmutableMap<String, HttpConnectorConfiguration> connectors = configuration.getConnectors();
        if (!connectors.isEmpty()) {
            message.append("HTTP Connectors:\n\t\t").append(connectors.entrySet()
                    .stream()
                    .map(HttpMessageBuilder::addConnector)
                    .collect(Collectors.joining("\n\t\t")));
        }
        return message.toString();
    }

    private String addConnector(Map.Entry<String, HttpConnectorConfiguration> entry) {
        StringBuilder message = new StringBuilder(entry.getKey()).append(SPACE + DASH + SPACE);
        String url = entry.getValue().getUrl();
        return message.append(HTTP_SCHEME)
                .append(SCHEME_DELIMITER)
                .append(url)
                .toString();
    }
}
