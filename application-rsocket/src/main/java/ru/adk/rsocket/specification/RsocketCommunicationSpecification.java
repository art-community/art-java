package ru.adk.rsocket.specification;

import ru.adk.rsocket.model.RsocketCommunicationTargetConfiguration;
import ru.adk.service.Specification;
import static ru.adk.core.constants.StringConstants.COLON;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_COMMUNICATION_SERVICE_TYPE;
import static ru.adk.rsocket.module.RsocketModule.rsocketModule;
import java.util.List;

public interface RsocketCommunicationSpecification extends Specification {
    default RsocketCommunicationTargetConfiguration communicationTarget(String serviceId) {
        return rsocketModule().getCommunicationTargetConfiguration(serviceId);
    }

    default String serviceId(String host, int port) {
        return host + COLON + port;
    }

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(RSOCKET_COMMUNICATION_SERVICE_TYPE);
    }
}