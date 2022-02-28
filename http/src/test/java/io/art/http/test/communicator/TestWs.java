package io.art.http.test.communicator;

import io.art.communicator.*;
import reactor.core.publisher.*;

public interface TestWs extends Communicator {
    void ws1();

    String ws2();

    Mono<String> ws3();

    Flux<String> ws4();

    void ws5(String input);

    String ws6(String input);

    Mono<String> ws7(String input);

    Flux<String> ws8(String input);

    void ws9(Mono<String> input);

    String ws10(Mono<String> input);

    Mono<String> ws11(Mono<String> input);

    Flux<String> ws12(Mono<String> input);

    void ws13(Flux<String> input);

    String ws14(Flux<String> input);

    Mono<String> ws15(Flux<String> input);

    Flux<String> ws16(Flux<String> input);

    void ws17(Flux<String> input);

    Flux<String> wsEcho(Flux<String> input);
}
