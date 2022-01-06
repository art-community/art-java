package io.art.tarantool.test;

import io.art.communicator.*;
import io.art.tarantool.test.model.*;
import reactor.core.publisher.*;
import java.util.*;
import java.util.stream.*;

public interface UserStorage extends Connector {
    UserSpace testSpace();

    interface UserSpace extends Communicator {
        User saveUser(User request);

        User getUser(int id);

        User deleteUser(int id);

        Stream<User> getAllUsers();

        Flux<List<User>> findTestUsers();
    }
}
