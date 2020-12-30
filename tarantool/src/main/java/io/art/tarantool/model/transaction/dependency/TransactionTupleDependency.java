package io.art.tarantool.model.transaction.dependency;

import lombok.AllArgsConstructor;

import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.core.factory.MapFactory.mapOf;

@AllArgsConstructor
public class TransactionTupleDependency implements TarantoolTransactionDependency {
    private final Integer transactionEntryNumber;

    @Override
    public Object get() {
        return mapOf("dependency", linkedListOf(transactionEntryNumber + 1));
    }
}
