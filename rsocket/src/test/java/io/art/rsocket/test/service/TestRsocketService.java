package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;

public class TestRsocketService implements TestRsocket {
    public void m1(String input) {
        System.out.println(input);
    }

    @Override
    public void m2(String input) {

    }
}
