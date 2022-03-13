package io.art.core.model;

import java.util.*;

public interface Tuple {
    List<Object> values();

    default int size() {
        return values().size();
    }

    static <V1> Tuple1<V1> tuple(V1 value1) {
        return new Tuple1<>(value1);
    }

    static <V1, V2> Tuple2<V1, V2> tuple(V1 value1, V2 value2) {
        return new Tuple2<>(value1, value2);
    }

    static <V1, V2, V3> Tuple3<V1, V2, V3> tuple(V1 value1,
                                                 V2 value2,
                                                 V3 value3) {
        return new Tuple3<>(value1, value2, value3);
    }

    static <V1, V2, V3, V4> Tuple4<V1, V2, V3, V4> tuple(V1 value1,
                                                         V2 value2,
                                                         V3 value3,
                                                         V4 value4) {
        return new Tuple4<>(value1, value2, value3, value4);
    }

    static <V1, V2, V3, V4, V5> Tuple5<V1, V2, V3, V4, V5> tuple(V1 value1,
                                                                 V2 value2,
                                                                 V3 value3,
                                                                 V4 value4,
                                                                 V5 value5) {
        return new Tuple5<>(value1, value2, value3, value4, value5);
    }
}
