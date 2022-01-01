package io.art.tarantool;

import io.art.tarantool.configuration.*;
import io.art.tarantool.transport.*;
import io.art.transport.module.*;
import org.junit.jupiter.api.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProtocolConstants.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

public class TarantoolTest {
    @BeforeAll
    public static void setup() {
        initialize(logging(), TransportActivator.transport(), tarantool());
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }


    @Test
    public void test() {
        TarantoolInstanceConfiguration configuration = TarantoolInstanceConfiguration.builder()
                .host("localhost")
                .port(3301)
                .username("username")
                .password("password")
                .connectionTimeout(30)
                .build();
        new TarantoolClient(configuration).connect().subscribe(client -> {
            Map<IntegerValue, Value> body = new HashMap<>();
            body.put(newInteger(IPROTO_FUNCTION_NAME), newString("art.test"));
            client.send(Mono.just(newMap(body)));
        });
        block();
    }
}
