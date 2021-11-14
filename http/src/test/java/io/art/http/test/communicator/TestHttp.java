package io.art.http.test.communicator;

import io.art.communicator.*;
import io.art.http.communicator.*;
import reactor.core.publisher.*;

public interface TestHttp extends HttpCommunicator<TestHttp> {
    void post1();

    String post2();

    Mono<String> post3();

    Flux<String> post4();

    void post5(String input);

    String post6(String input);

    Mono<String> post7(String input);

    Flux<String> post8(String input);

    void post9(Mono<String> input);

    String post10(Mono<String> input);

    Mono<String> post11(Mono<String> input);

    Flux<String> post12(Mono<String> input);

    void post13(Flux<String> input);

    String post14(Flux<String> input);

    Mono<String> post15(Flux<String> input);

    Flux<String> post16(Flux<String> input);

    void post17(Flux<String> empty);

    String post18(Flux<String> empty);

    Mono<String> post19(Flux<String> empty);

    Flux<String> post20(Flux<String> empty);

    String getFile();

    interface TestHttpConnector extends Connector {
        TestHttp testHttp();
    }
}
