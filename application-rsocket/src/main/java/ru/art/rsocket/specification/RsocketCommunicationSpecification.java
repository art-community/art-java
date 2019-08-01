package ru.art.rsocket.specification;

import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import ru.art.service.Specification;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_COMMUNICATION_SERVICE_TYPE;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
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