package io.art.core.extensions;

import io.art.core.caster.*;
import io.art.core.constants.*;
import io.art.core.exception.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;

@UtilityClass
public class ReactiveExtensions {
    public Mono<Void> mono(Runnable action) {
        return Mono.create(emitter -> {
            action.run();
            emitter.success(null);
        });
    }

    public Flux<Void> flux(Runnable action) {
        return create(emitter -> {
            action.run();
            emitter.complete();
        });
    }

    public <T> Flux<T> adoptFlux(MethodProcessingMode mode, T object) {
        if (isNull(mode)) throw new ImpossibleSituationException();
        switch (mode) {
            case BLOCKING:
                return let(object, Flux::just, Flux.empty());
            case MONO:
                return let(object, input -> Flux.from(cast(input)), Flux.empty());
            case FLUX:
                return cast(object);
        }
        throw new ImpossibleSituationException();
    }

    public <T> Mono<T> adoptMono(MethodProcessingMode mode, T object) {
        if (isNull(mode)) throw new ImpossibleSituationException();
        switch (mode) {
            case BLOCKING:
                return let(object, Mono::just, Mono.empty());
            case MONO:
                return cast(object);
            case FLUX:
                return let(Caster.<Flux<T>>cast(object), Flux::next, Mono.empty());
        }
        throw new ImpossibleSituationException();
    }
}
