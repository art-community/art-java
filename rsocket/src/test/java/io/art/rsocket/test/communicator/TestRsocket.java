package io.art.rsocket.test.communicator;

import io.art.communicator.model.*;

public interface TestRsocket extends Communicator {
    void m(String req);

    void m2(String req);
}
