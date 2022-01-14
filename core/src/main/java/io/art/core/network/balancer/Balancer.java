package io.art.core.network.balancer;

import java.util.*;

public interface Balancer<T> {
    T select();

    Collection<T> endpoints();

    void endpoints(Collection<T> endpoints);

    void addEndpoint(T endpoint);

    enum BalancerMethod {
        ROUND_ROBIN
    }
}
