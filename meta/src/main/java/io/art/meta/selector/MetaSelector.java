package io.art.meta.selector;

import io.art.meta.model.*;

public class MetaSelector {
    private final ThreadLocal<MetaClass<?>> type = new ThreadLocal<>();

    public <T extends MetaClass<?>> T type(T type) {
        this.type.set(type);
        return type;
    }


    @FunctionalInterface
    public interface MetaSelectorTypeConsumer {
        MetaClass<?> consume(MetaSelector selector);
    }


    @FunctionalInterface
    public interface MetaSelectorFieldConsumer {
        MetaField<?> consume(MetaSelector selector);
    }

    @FunctionalInterface
    public interface MetaSelectorMethodConsumer {
        MetaMethod<?> consume(MetaSelector selector);
    }

    public MetaClass<?> type() {
        return type.get();
    }
}
