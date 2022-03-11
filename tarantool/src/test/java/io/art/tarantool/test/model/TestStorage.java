package io.art.tarantool.test.model;

import io.art.tarantool.communicator.*;
import reactor.core.publisher.*;

public interface TestStorage extends TarantoolStorage<TestStorage> {
    void testSubscription();

    Flux<String> testChannel();

    String testMapper();
}
