package io.art.storage.index;

import java.util.function.*;

public interface IndexesProvider<T> extends Supplier<Indexes<T>> {
    Class<T> type();
}
