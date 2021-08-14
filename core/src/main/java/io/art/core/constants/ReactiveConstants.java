package io.art.core.constants;

import reactor.core.publisher.*;

public interface ReactiveConstants {
    Object NULL_OBJECT = new Object();
    Flux<?> NULL_FLUX = Flux.just(NULL_OBJECT);
    Mono<?> NULL_MONO = Mono.just(NULL_OBJECT);
}
