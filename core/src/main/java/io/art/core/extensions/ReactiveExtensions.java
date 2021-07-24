package io.art.core.extensions;

import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;

@UtilityClass
public class ReactiveExtensions {
    public Mono<Void> mono(Runnable action) {
        return Mono.create(emitter -> {
            action.run();
            emitter.success(null);
        });
    }

    public Flux<Void> flux(Runnable action) {
        return Flux.create(emitter -> {
            action.run();
            emitter.complete();
        });
    }

    public <T> Mono<T> asMono(Object object) {
        return cast(object);
    }

    public <T> Flux<T> asFlux(Object object) {
        return cast(object);
    }
}
