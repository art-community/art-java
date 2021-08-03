package io.art.rsocket.test.communicator;

import io.art.communicator.model.*;

public interface TestRsocket extends Communicator {
    void m1(String input);

    void m2(String input);

    void m3(String input);
}
