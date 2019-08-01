package ru.art.network.manager.refresher;

import ru.art.service.ServiceController;
import ru.art.state.api.model.ClusterProfileRequest;
import ru.art.state.api.model.ClusterProfileResponse;
import static java.lang.System.getProperty;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ContextConstants.LOCAL_PROFILE;
import static ru.art.core.constants.SystemProperties.PROFILE_PROPERTY;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.LoggingMessages.REFRESHING_UPDATE;
import static ru.art.network.manager.module.NetworkManagerModule.networkManagerModuleState;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.Methods.GET_CLUSTER_PROFILE;
import static ru.art.state.api.constants.StateApiConstants.NetworkServiceConstants.NETWORK_SERVICE_ID;

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
