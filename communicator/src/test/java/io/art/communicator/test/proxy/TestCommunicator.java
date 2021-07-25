package io.art.communicator.test.proxy;

import reactor.core.publisher.*;

public interface TestCommunicator {
    void m1();

    String m2();

    Mono<String> m3();

    Flux<String> m4();
    
    void m5(String input);

    String m6(String input);

    Mono<String> m7(String input);

    Flux<String> m8(String input);

    void m9(Mono<String> input);

    String m10(Mono<String> input);

    Mono<String> m11(Mono<String> input);

    Flux<String> m12(Mono<String> input);


    void m13(Flux<String> input);

    String m14(Flux<String> input);

    Mono<String> m15(Flux<String> input);

    Flux<String> m16(Flux<String> input);
}
