package io.art.tarantool.transaction;

import io.art.tarantool.transaction.operation.result.TarantoolTransactionOperationResult;

import java.util.List;

import static io.art.core.factory.ListFactory.linkedList;

public class TarantoolTransactionState {
    private boolean activeTransaction = false;
    private boolean isRWTransaction = false;
    private final List<List<?>> operations = linkedList();
    private final List<TarantoolTransactionOperationResult<?>> results = linkedList();
}
