package ru.art.configurator.api.constants;

public interface ConfiguratorServiceConstants {
    String CONFIGURATOR_PATH = "ru.art.configurator.ConfiguratorGrpcServlet";
    String CONFIGURATOR_SERVICE_ID = "CONFIGURATOR_SERVICE";
    String USER_SERVICE_ID = "USER_SERVICE";

    interface Methods {
        String LOGIN = "LOGIN";
        String CHECK_TOKEN = "CHECK_TOKEN";
        String UPLOAD_CONFIG = "UPLOAD_CONFIG";
        String GET_PROTOBUF_CONFIG = "GET_PROTOBUF_CONFIG";
        String GET_JSON_CONFIG = "GET_JSON_CONFIG";
        String APPLY_MODULE_CONFIG = "APPLY_MODULE_CONFIG";
        String GET_ALL_PROFILES = "GET_ALL_PROFILES";
        String GET_ALL_MODULES = "GET_ALL_MODULES";
        String UPLOAD_APPLICATION_CONFIG = "UPLOAD_APPLICATION_CONFIG";
        String UPLOAD_PROFILE_CONFIG = "UPLOAD_PROFILE_CONFIG";
        String UPLOAD_MODULE_CONFIG = "UPLOAD_MODULE_CONFIG";
        String GET_APPLICATION_CONFIG = "GET_APPLICATION_CONFIG";
    }
}
