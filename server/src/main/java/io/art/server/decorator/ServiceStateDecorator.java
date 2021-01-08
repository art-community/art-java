package io.art.server.decorator;

import io.art.server.module.*;
import io.art.server.specification.*;
import io.art.server.state.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.server.constants.ServerModuleConstants.StateKeys.*;
import static io.art.server.state.ServerModuleState.ServerThreadLocalState.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class ServiceStateDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ServerModuleState moduleState = ServerModule.serverModule().state();
    private final ServiceMethodSpecification specification;

    public ServiceStateDecorator(ServiceMethodSpecification specification) {
        this.specification = specification;
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return input
                .materialize()
                .doOnNext(signal -> getModuleState().localState(fromContext(signal.getContext())))
                .dematerialize()
                .subscriberContext(context -> context.putNonNull(SPECIFICATION_KEY, specification));
    }
}
