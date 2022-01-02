package io.art.tarantool.test;

import io.art.logging.*;
import io.art.message.pack.descriptor.*;
import io.art.meta.module.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import io.art.tarantool.transport.*;
import org.junit.jupiter.api.*;
import reactor.core.publisher.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.factory.TarantoolRequestContentFactory.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.transport.module.TransportActivator.*;

public class TarantoolTest {

    private final MessagePackWriter writer = new MessagePackWriter();
    private final MessagePackReader reader = new MessagePackReader();

    @BeforeAll
    public static void setup() {
        initialize(logging(), MetaActivator.meta(MetaTarantoolTest::new), transport(), tarantool());
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
        new TarantoolClient(configuration)
                .connect()
                .flatMap(client -> client.send(Mono.just(callRequest("tryRequest", writer.write(definition(TestRequest.class), new TestRequest("mew"))))).map(response -> reader.read(definition(TestRequest.class), response)))
                .subscribe(response -> Logging.logger().info(response.toString()), Logging.logger()::error);
        block();
    }
}
