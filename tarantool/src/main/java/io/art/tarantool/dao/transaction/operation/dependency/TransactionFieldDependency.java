package io.art.tarantool.dao.transaction.operation.dependency;

import lombok.AllArgsConstructor;

import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.core.factory.MapFactory.mapOf;

@AllArgsConstructor
public class TransactionFieldDependency implements TarantoolTransactionDependency {
    private final int transactionEntryNumber;
    private final String fieldName;

    @Override
    public Object get() {
        return mapOf("dependency", linkedListOf(transactionEntryNumber + 1, fieldName));
    }
}
