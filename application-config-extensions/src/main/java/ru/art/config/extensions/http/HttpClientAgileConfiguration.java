package ru.art.config.extensions.http;

import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import ru.art.http.client.configuration.HttpClientModuleConfiguration.HttpClientModuleDefaultConfiguration;
import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mime.MimeType;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.http.HttpConfigKeys.*;
import static ru.art.config.extensions.http.HttpContentMappersConfigurator.configureHttpContentMappers;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.http.client.constants.HttpClientModuleConstants.DEFAULT_TIMEOUT;
import static ru.art.http.constants.HttpCommonConstants.HTTP_SCHEME;
import java.util.Map;

@Getter
public class HttpClientAgileConfiguration extends HttpClientModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureHttpContentMappers(super.getContentMappers());
    private boolean enableTracing;
    private RequestConfig requestConfig;
    private SocketConfig socketConfig;
    private IOReactorConfig ioReactorConfig;
    private String balancerHost;
    private int balancerPort;
    private Map<String, HttpCommunicationTargetConfiguration> communicationTargets;
    private boolean ssl;
    private boolean disableSslHostNameVerification;
    private String sslKeyStoreType;
    private String sslKeyStoreFilePath;
    private String sslKeyStorePassword;

    public HttpClientAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableTracing = configBoolean(HTTP_COMMUNICATION_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        balancerHost = configString(HTTP_BALANCER_SECTION_ID, HOST, super.getBalancerHost());
        balancerPort = configInt(HTTP_BALANCER_SECTION_ID, PORT, super.getBalancerPort());
        communicationTargets = configMap(HTTP_COMMUNICATION_SECTION_ID, TARGETS, config -> HttpCommunicationTargetConfiguration.builder()
                .host(ifEmpty(config.getString(HOST), balancerHost))
                .port(getOrElse(config.getInt(PORT), balancerPort))
                .path(getOrElse(config.getString(PATH), SLASH))
                .scheme(ifEmpty(config.getString(SCHEME), HTTP_SCHEME))
                .url(config.getString(URL))
                .requestConfig(RequestConfig.custom()
                        .setConnectTimeout(getOrElse(config.getInt(CONNECTION_TIMEOUT), super.getRequestConfig().getConnectTimeout()))
                        .setSocketTimeout(getOrElse(config.getInt(SOCKET_TIMEOUT), super.getRequestConfig().getSocketTimeout()))
                        .setConnectionRequestTimeout(getOrElse(config.getInt(CONNECTION_REQUEST_TIMEOUT), super.getRequestConfig().getConnectionRequestTimeout()))
                        .build())
                .build(), super.getCommunicationTargets());
        int socketTimeout = configInt(HTTP_COMMUNICATION_SECTION_ID, SOCKET_TIMEOUT, DEFAULT_TIMEOUT);
        int connectionTimeout = configInt(HTTP_COMMUNICATION_SECTION_ID, CONNECTION_TIMEOUT, DEFAULT_TIMEOUT);
        int connectionRequestTimeout = configInt(HTTP_COMMUNICATION_SECTION_ID, CONNECTION_REQUEST_TIMEOUT, DEFAULT_TIMEOUT);
        int ioReactorThreadCount = configInt(HTTP_COMMUNICATION_SECTION_ID, THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE);
        ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(ioReactorThreadCount)
                .build();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
        socketConfig = SocketConfig.custom()
                .setSoTimeout(socketTimeout)
                .build();
        ssl = configBoolean(HTTP_COMMUNICATION_SECTION_ID, SSL, super.isSsl());
        disableSslHostNameVerification = configBoolean(HTTP_COMMUNICATION_SECTION_ID, IS_DISABLE_SSL_HOST_NAME_VERIFICATION, super.isDisableSslHostNameVerification());
        sslKeyStoreType = configString(HTTP_COMMUNICATION_SECTION_ID, SSL_KEY_STORE_TYPE, super.getSslKeyStoreType());
        sslKeyStoreFilePath = configString(HTTP_COMMUNICATION_SECTION_ID, SSL_KEY_STORE_FILE_PATH, super.getSslKeyStoreFilePath());
        sslKeyStorePassword = configString(HTTP_COMMUNICATION_SECTION_ID, SSL_KEY_STORE_PASSWORD, super.getSslKeyStorePassword());
    }
}