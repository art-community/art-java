package ru.adk.network.manager.balancer;

import ru.adk.network.manager.constants.NetworkManagerModuleConstants.BalancerMode;
import ru.adk.network.manager.exception.UnknownBalancerModeException;
import ru.adk.state.api.model.ModuleEndpoint;
import java.util.Collection;
import java.util.Map;

public interface Balancer {
    static Balancer balancerOf(BalancerMode mode) {
        switch (mode) {
            case ROUND_ROBIN:
                return new RoundRobinBalancer();
            case FEWER_SESSIONS:
                return new SessionsBalancer();
        }
        throw new UnknownBalancerModeException(mode);
    }

    ModuleEndpoint select(String modulePath);

    Collection<ModuleEndpoint> getEndpoints(String modulePath);

    void updateEndpoints(Map<String, Collection<ModuleEndpoint>> moduleEndpoints);
}
