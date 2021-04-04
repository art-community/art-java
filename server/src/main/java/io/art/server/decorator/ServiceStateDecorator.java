package io.art.server.decorator;

import io.art.server.specification.*;
import io.art.server.state.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.util.context.*;
import static io.art.server.constants.ServerModuleConstants.StateKeys.*;
import static io.art.server.module.ServerModule.*;
import static io.art.server.state.ServerModuleState.ServerThreadLocalState.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@RequiredArgsConstructor
public class ServiceStateDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ServerModuleState moduleState = serverModule().state();
    private final ServiceMethodSpecification specification;

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return input.doOnEach(signal -> loadContext(signal.getContextView())).contextWrite(this::saveContext);
    }

    private void loadContext(ContextView context) {
        getModuleState().localState(fromContext(context));
    }

    private Context saveContext(Context context) {
        return context.putNonNull(SPECIFICATION_KEY, specification);
    }
}
