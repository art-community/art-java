package io.art.tarantool.space;

import io.art.storage.*;

public class TarantoolDefaultStorage implements Storage {
    interface TarantoolBox extends TarantoolSpace<TarantoolBox> {

    }

    interface TarantoolStream extends TarantoolSpace<TarantoolStream> {

    }
}
