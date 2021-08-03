package io.art.rsocket.test.communicator;

import io.art.communicator.model.*;

public interface TestRsock extends Communicator {
    void m(String req);
}
