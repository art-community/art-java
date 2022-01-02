package io.art.tarantool.test;

import io.art.communicator.*;
import io.art.tarantool.test.model.*;

public interface UserStorage extends Connector {
    UserSpace testSpace();

    interface UserSpace extends Communicator {
        User saveUser(User request);

        User getUser(int id);
    }
}
