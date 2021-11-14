package io.art.core.factory;

import io.art.core.model.*;
import lombok.experimental.*;

@UtilityClass
public class PairFactory {
    public static <K, V> Pair<K, V> pairOf(K k, V v) {
        return new Pair<>(k, v);
    }
}
