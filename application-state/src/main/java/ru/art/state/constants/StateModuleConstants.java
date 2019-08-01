package ru.art.state.constants;

public interface StateModuleConstants {
    String APPLICATION_STATE_MODULE_ID = "APPLICATION_STATE_MODULE";
    int DEFAULT_MODULE_ENDPOINT_CHECK_RATE_SECONDS = 10;
    int DEFAULT_MODULE_ENDPOINT_LIFE_TIME_MINUTES = 2;

    interface DbKeys {
        String CLUSTER = "CLUSTER";
    }
}
