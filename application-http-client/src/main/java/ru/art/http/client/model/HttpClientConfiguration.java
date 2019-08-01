package ru.art.http.client.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;

@Getter
@Builder
public class HttpClientConfiguration {
    @Builder.Default
    private final RequestConfig requestConfig = httpClientModule().getRequestConfig();
    @Builder.Default
    private final SocketConfig socketConfig = httpClientModule().getSocketConfig();
    @Builder.Default
    private final IOReactorConfig ioReactorConfig = httpClientModule().getIoReactorConfig();
    @Builder.Default
    private final ConnectionConfig connectionConfig = httpClientModule().getConnectionConfig();
    @Builder.Default
    private final boolean ssl = httpClientModule().isSsl();
    @Builder.Default
    private final boolean disableSslHostNameVerification = httpClientModule().isDisableSslHostNameVerification();
    @Builder.Default
    private final String sslKeyStoreType = httpClientModule().getSslKeyStoreType();
    @Builder.Default
    private final String sslKeyStoreFilePath = httpClientModule().getSslKeyStoreFilePath();
    @Builder.Default
    private final String sslKeyStorePassword = httpClientModule().getSslKeyStorePassword();
    @Builder.Default
    private final boolean enableTracing = false;
}