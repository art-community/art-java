package io.art.rsocket.test.communicator;

import io.art.communicator.model.*;

public interface TestRsocketConnector extends Connector {
    TestRsocketCommunicator testRsocket();

    TestRsocketOtherCommunicator testOtherRsocket();
}
