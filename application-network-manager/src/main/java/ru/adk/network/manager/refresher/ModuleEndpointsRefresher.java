package ru.adk.network.manager.refresher;

import ru.adk.service.ServiceController;
import ru.adk.state.api.model.ClusterProfileRequest;
import ru.adk.state.api.model.ClusterProfileResponse;
import static java.lang.System.getProperty;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ContextConstants.LOCAL_PROFILE;
import static ru.adk.core.constants.SystemProperties.PROFILE_PROPERTY;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.network.manager.constants.NetworkManagerModuleConstants.LoggingMessages.REFRESHING_UPDATE;
import static ru.adk.network.manager.module.NetworkManagerModule.networkManagerModuleState;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.GET_CLUSTER_PROFILE;
import static ru.adk.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_SERVICE_ID;

public interface ModuleEndpointsRefresher {
    static void refreshModuleEndpoints() {
        try {
            String profile;
            ServiceController
                    .<ClusterProfileRequest, ClusterProfileResponse>executeServiceMethod(NETWORK_SERVICE_ID, GET_CLUSTER_PROFILE, ClusterProfileRequest.builder()
                            .profile(isEmpty(profile = getProperty(PROFILE_PROPERTY)) ? LOCAL_PROFILE : profile)
                            .build())
                    .map(ClusterProfileResponse::getModuleEndpointStates)
                    .ifPresent(networkManagerModuleState().getClusterState()::updateModuleEndpoints);
        } catch (Exception e) {
            loggingModule()
                    .getLogger(ModuleEndpointsRefresher.class)
                    .error(REFRESHING_UPDATE, e);
        }
    }
}
