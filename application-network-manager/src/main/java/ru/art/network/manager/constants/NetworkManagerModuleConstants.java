package ru.art.network.manager.constants;

public interface NetworkManagerModuleConstants {
    String NETWORK_MANAGER_MODULE_ID = "NETWORK_MANAGER_MODULE";
    int DEFAULT_STATE_PORT = 8000;

    enum BalancerMode {
        ROUND_ROBIN,
        FEWER_SESSIONS
    }

    interface ExceptionMessages {
        String UNKNOWN_BALANCER_MODE_MESSAGE = "Unknown balancer mode: ''{0}''";
    }

    interface LoggingMessages {
        String REFRESHING_UPDATE = "Error occurred during refreshing endpoints";
    }
}
