package io.art.rsocket.test.communicator;

import io.art.core.communication.*;

public interface TestRsocket extends Communicator {
    void m1(String input);

    void m2(String input);

    void m3(String input);

    void m4(String input);

    interface TestRsocketConnector extends Connector {
        TestRsocket testRsocket();
    }
}
