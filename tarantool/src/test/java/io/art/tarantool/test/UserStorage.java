package io.art.tarantool.test;

import io.art.storage.*;
import io.art.tarantool.communication.*;
import io.art.tarantool.test.model.*;
import reactor.core.publisher.*;
import java.util.*;
import java.util.stream.*;

public interface UserStorage extends Storage {
    UserSpace testSpace();

    interface UserSpace extends TarantoolSpace<UserSpace> {
        Mono<User> saveUser(User request);

        Flux<User> getUser(int id);

        User deleteUser(int id);

        Stream<User> getAllUsers();

        Flux<List<User>> findTestUsers();
    }
}
