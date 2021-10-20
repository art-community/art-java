package io.art.tarantool.model.record;

import io.art.tarantool.model.transaction.dependency.*;
import java.util.*;
import java.util.function.*;

public interface TarantoolRecord<T> extends Record<T> {

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    <U> TarantoolRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper);

    TarantoolRecord<T> synchronize();
}
