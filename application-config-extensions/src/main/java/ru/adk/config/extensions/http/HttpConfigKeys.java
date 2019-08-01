package ru.adk.config.extensions.http;

public interface HttpConfigKeys {
    String HTTP_SERVER_SECTION_ID = "http.server";
    String HTTP_COMMUNICATION_SECTION_ID = "http.communication";
    String HTTP_BALANCER_SECTION_ID = "http.balancer";
    String MAX_THREADS_COUNT = "maxThreadsCount";
    String MIN_SPARE_THREADS_COUNT = "minSpareThreadsCount";
    String WEB_URL = "webUrl";
    String CONNECTION_TIMEOUT = "connectionTimeout";
    String CONNECTION_REQUEST_TIMEOUT = "connectionRequestTimeout";
    String SOCKET_TIMEOUT = "socketTimeout";
    String SCHEME = "scheme";
    String IS_DISABLE_SSL_HOST_NAME_VERIFICATION = "disableSslHostNameVerification";
    String SSL = "ssl";
    String SSL_KEY_STORE_FILE_PATH = "sslKeyStoreFilePath";
    String SSL_KEY_STORE_PASSWORD = "sslKeyStorePassword";
    String SSL_KEY_STORE_TYPE = "sslKeyStoreType";
}
