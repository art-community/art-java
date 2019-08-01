package ru.adk.http.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.http.client.config.RequestConfig;
import static ru.adk.http.client.module.HttpClientModule.httpClientModule;
import static ru.adk.http.constants.HttpCommonConstants.HTTP_SCHEME;

@Getter
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class HttpCommunicationTargetConfiguration {
    @Setter
    @Accessors(fluent = true)
    private String url;
    @Setter
    private String path;
    @Builder.Default
    private final String host = httpClientModule().getBalancerHost();
    @Builder.Default
    private final Integer port = httpClientModule().getBalancerPort();
    @Builder.Default
    private final RequestConfig requestConfig = httpClientModule().getRequestConfig();
    @Builder.Default
    private final String scheme = HTTP_SCHEME;

    public HttpCommunicationTargetConfiguration addPath(String path) {
        this.path = this.path + path;
        return this;
    }
}
