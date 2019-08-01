package ru.adk.config.extensions.http;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.http.HttpConfigKeys.HTTP_COMMUNICATION_SECTION_ID;
import static ru.adk.config.extensions.http.HttpConfigKeys.HTTP_SERVER_SECTION_ID;

public interface HttpConfigProvider {
    static Config httpServerConfig() {
        return configInner(HTTP_SERVER_SECTION_ID);
    }

    static Config httpCommunicationConfig() {
        return configInner(HTTP_COMMUNICATION_SECTION_ID);
    }
}
