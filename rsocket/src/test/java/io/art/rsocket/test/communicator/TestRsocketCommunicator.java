package io.art.rsocket.test.communicator;

import io.art.communicator.model.*;

public interface TestRsocketCommunicator extends Communicator {
    void m(String req);
}
