package ru.art.information.constants;

import static ru.art.http.constants.HttpCommonConstants.API_PATH;

public interface InformationModuleConstants {
    String INFORMATION_PATH = "/information";
    String GET_INFORMATION_PATH = INFORMATION_PATH + API_PATH + "/get";
    String STATUS_PATH = "/status";
    String HTTP_SERVER_WAS_NOT_INITIALIZED = "HTTP Server was not initialized";
    String GRPC_SERVER_WAS_NOT_INITIALIZED = "GRPC Server was not initialized";
    String INFORMATION_MODULE_ID = "INFORMATION_MODULE";
    String INFORMATION_INDEX_HTML = "information.index.html";
    String MAIN_MODULE_ID_VARIABLE =  "mainModuleId";
}
