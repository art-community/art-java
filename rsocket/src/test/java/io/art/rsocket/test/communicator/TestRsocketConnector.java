package io.art.rsocket.test.communicator;

public interface TestRsocketConnector {
    TestRsocketCommunicator testRsocket();

    TestRsocketOtherCommunicator testOtherRsocket();
}
