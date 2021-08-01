package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;

public class TestRsocketService implements TestRsocketCommunicator {
    public void m(String req) {
        System.out.println(req);
    }
}
