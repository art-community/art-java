package io.art.tarantool.model.operation;

import lombok.*;

import java.util.*;

import static io.art.core.factory.ListFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectSortComparator.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TarantoolSortOperation {
    private final String comparatorFunction;
    private final Long fieldNumber;

    public static TarantoolSortOperation ascending(Long fieldNumber){
        return new TarantoolSortOperation(ASCENDING, fieldNumber);
    }

    public static TarantoolSortOperation descending(Long fieldNumber){
        return new TarantoolSortOperation(DESCENDING, fieldNumber);
    }

    public static TarantoolSortOperation custom(String functionDefinition, Long fieldNumber){
        return new TarantoolSortOperation(functionDefinition, fieldNumber);
    }

    public List<Object> build(){
        return linkedListOf(SORT, linkedListOf(comparatorFunction, fieldNumber));
    }
}
