package ru.adk.rsocket.function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.adk.reactive.service.model.ReactiveService;
import ru.adk.rsocket.service.RsocketService;
import ru.adk.rsocket.specification.RsocketReactiveServiceSpecification;
import static ru.adk.core.caster.Caster.cast;
import java.util.function.Function;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class RsocketFunctionalServiceSpecification implements RsocketReactiveServiceSpecification {
    private final String serviceId;
    private final RsocketService rsocketService;
    private ReactiveService reactiveService;
    private final Function<?, ?> function;


    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        return cast(function.apply(cast(request)));
    }
}