package io.art.tarantool.test;

import io.art.logging.*;
import io.art.tarantool.test.UserStorage.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.tarantool.Tarantool.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.transport.module.TransportActivator.*;

public class TarantoolTest {
    @BeforeAll
    public static void setup() {
        initialize(logging(),
                meta(MetaTarantoolTest::new),
                transport(),
                tarantool(tarantool -> tarantool
                        .storage(UserStorage.class, storage -> storage.client(client -> client.username("test").password("test"))))
        );
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }

    @Test
    public void test() {
        UsersSpace space = tarantoolStorage(UserStorage.class).testSpace().mutable().immutable();
        space.save(User.builder().id(4).name("test 4").address(new User.Address(123)).build());
        User user = space.get(4).blockFirst();
        space.save(user.toBuilder().address(user.getAddress().toBuilder().house(12).build()).build());

        Logging.logger().info("test: ");
        space.findTest().subscribe(value -> Logging.logger().info(value.toString()));
        Logging.logger().info("all: ");
        space.getAll().forEach(value -> Logging.logger().info(value.toString()));
    }
}
