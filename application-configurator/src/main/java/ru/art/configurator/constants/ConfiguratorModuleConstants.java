package ru.art.configurator.constants;

import static ru.art.core.factory.CollectionsFactory.setOf;
import java.util.Set;

public interface ConfiguratorModuleConstants {
    String CONFIGURATOR_MODULE_ID = "CONFIGURATOR_MODULE";
    String HTTP_SERVER_BOOTSTRAP_THREAD = "http-server-bootstrap-thread";
    String GRPC_INSTANCES = ".grpc.instances";

    String APPLICATION_BALANCER_HOST_CONFIG_KEY = "balancer.host";
    String APPLICATION_BALANCER_PORT_CONFIG_KEY = "balancer.port";
    String HTTP_PATH = "/configurator";
    String APPLICATION_MODULE_GRPC_PATH_CONFIG_KEY = "protobuf.server.path";
    String TOKEN_COOKIE = "TOKEN";
    String LOGIN_PATH = "/login";
    String CHECK_TOKEN_PATH = "/checkToken";
    Set<String> AUTHORIZATION_CHECKING_URLS = setOf("/upload",
            "/get",
            "/getApplicationConfiguration",
            "/apply",
            "/profiles",
            "/modules",
            "/uploadApplication",
            "/uploadProfile",
            "/uploadModule");
    String UPLOAD_PATH = "/upload";
    String GET_PATH = "/get";
    String GET_APPLICATION_CONFIGURATION_PATH = "/getApplicationConfiguration";
    String APPLY_PATH = "/apply";
    String PROFILES_PATH = "/profiles";
    String MODULES_PATH = "/modules";
    String UPLOAD_APPLICATION_PATH = "/uploadApplication";
    String UPLOAD_PROFILE_PATH = "/uploadProfile";
    String UPLOAD_MODULE_PATH = "/uploadModule";

    interface ConfiguratorLocalConfigKeys {
        String CONFIGURATOR_SECTION_ID = "configurator";
        String CONFIGURATOR_HTTP_PORT_PROPERTY = "http.port";
        String CONFIGURATOR_GRPC_PORT_PROPERTY = "grpc.port";
        String CONFIGURATOR_WEB_URL_PROPERTY = "http.url";
        String CONFIGURATOR_USER = "user";
        String CONFIGURATOR_PASSWORD = "password";
        String CONFIGURATOR_ROCKS_DB_PATH = "rocks.db.path";
    }
}
