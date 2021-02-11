package io.art.core.extensions;

import lombok.experimental.*;
import reactor.core.publisher.*;

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
}
