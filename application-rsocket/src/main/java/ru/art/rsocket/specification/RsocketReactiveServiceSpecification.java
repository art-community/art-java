package ru.art.rsocket.specification;

import ru.art.reactive.service.specification.ReactiveServiceSpecification;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import java.util.List;

public interface RsocketReactiveServiceSpecification extends RsocketServiceSpecification, ReactiveServiceSpecification {
    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(REACTIVE_SERVICE_TYPE, RSOCKET_SERVICE_TYPE);
    }

}
