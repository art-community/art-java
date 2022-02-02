package io.art.tarantool.test;

import io.art.tarantool.*;
import io.art.tarantool.service.*;
import io.art.tarantool.test.meta.*;
import io.art.tarantool.test.model.*;
import org.junit.jupiter.api.*;
import static io.art.core.context.Context.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.meta.module.MetaActivator.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.transport.module.TransportActivator.*;

public class TarantoolTest {
    @BeforeAll
    public static void setup() {
        initialize(logging(),
                meta(MetaTarantoolTest::new),
                transport(),
                tarantool(tarantool -> tarantool
                        .space(UserStorage.class, User.class, storage -> storage.client(client -> client.username("username").password("password"))))
        );
    }

    @AfterAll
    public static void cleanup() {
        shutdown();
    }

    @Test
    public void test() {
        TarantoolSpaceService<Integer, User> user = Tarantool.tarantool().space(User.class);
        user.put(User.builder().name("test").build());
        System.out.println(user.findFirst(1));
    }
}
