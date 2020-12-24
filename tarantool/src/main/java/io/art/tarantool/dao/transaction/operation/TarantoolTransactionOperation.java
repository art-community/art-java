package io.art.tarantool.dao.transaction.operation;

import java.util.List;

import static io.art.core.factory.ListFactory.linkedListOf;

public class TarantoolTransactionOperation {

    public static List<?> tarantoolTransactionOperation(String command, Object ... args){
        return linkedListOf(command, linkedListOf(args));
    }
}
