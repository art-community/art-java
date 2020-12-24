package io.art.tarantool.dao.transaction.entry;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;

import static io.art.core.factory.ListFactory.linkedList;
import static io.art.core.factory.ListFactory.linkedListOf;

public class TarantoolTransactionEntry {

    public static List<?> tarantoolTransactionEntry(String command, Object ... args){
        return linkedListOf(command, linkedListOf(args));
    }
}
