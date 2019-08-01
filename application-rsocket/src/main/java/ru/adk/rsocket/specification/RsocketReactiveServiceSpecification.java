package ru.adk.rsocket.specification;

import ru.adk.reactive.service.specification.ReactiveServiceSpecification;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_TYPE;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import java.util.List;

public interface RsocketReactiveServiceSpecification extends RsocketServiceSpecification, ReactiveServiceSpecification {
    @Override
    default List<String> getServiceTypes() {
        return fixedArrayOf(REACTIVE_SERVICE_TYPE, RSOCKET_SERVICE_TYPE);
    }

}
