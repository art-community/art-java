package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;

public class TestRsocketService implements TestRsocket {
    public void m(String req) {
        System.out.println(req);
    }

    @Override
    public void m2(String req) {

    }
}
