package ru.art.rsocket.specification;

import ru.art.rsocket.service.RsocketService;
import ru.art.service.Specification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import java.util.List;

public interface RsocketServiceSpecification extends Specification {
    RsocketService getRsocketService();

    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(RSOCKET_SERVICE_TYPE);
    }
}
