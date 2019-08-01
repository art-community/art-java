package ru.adk.rsocket.specification;

import ru.adk.rsocket.service.RsocketService;
import ru.adk.service.Specification;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import java.util.List;

public interface RsocketServiceSpecification extends Specification {
    RsocketService getRsocketService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(RSOCKET_SERVICE_TYPE);
    }
}
