package ru.art.rsocket.function;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.art.reactive.service.model.ReactiveService;
import ru.art.rsocket.service.RsocketService;
import ru.art.rsocket.specification.RsocketReactiveServiceSpecification;
import static ru.art.core.caster.Caster.cast;
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