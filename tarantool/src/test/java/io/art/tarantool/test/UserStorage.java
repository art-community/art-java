package io.art.tarantool.test;

import io.art.communicator.*;
import io.art.tarantool.test.model.*;
import java.util.*;

public interface UserStorage extends Connector {
    UserSpace testSpace();

    interface UserSpace extends Communicator {
        User saveUser(User request);

        User getUser(int id);

        User deleteUser(int id);

        List<User> getAllUsers();
    }
}
