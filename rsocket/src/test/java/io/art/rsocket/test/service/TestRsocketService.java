package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;

public class TestRsocketService implements TestRsocket {
    public void m1(String input) {
        System.out.println("m1");
    }

    @Override
    public void m2(String input) {
        System.out.println("m2");
    }

    @Override
    public void m3(String input) {
        System.out.println("m3");
    }

    @Override
    public void m4(String input) {

    }
}
