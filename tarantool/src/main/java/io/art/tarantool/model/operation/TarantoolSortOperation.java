package io.art.tarantool.model.operation;

import io.art.tarantool.model.field.*;
import lombok.*;

import java.util.*;

import static io.art.core.factory.ListFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectSortComparator.*;
import static java.util.Objects.nonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TarantoolSortOperation {
    private final String comparatorFunction;
    private Long fieldNumber;
    private final String fieldName;

    public static TarantoolSortOperation ascending(Long fieldNumber){
        return new TarantoolSortOperation(ASCENDING, fieldNumber, null);
    }

    public static TarantoolSortOperation descending(Long fieldNumber){
        return new TarantoolSortOperation(DESCENDING, fieldNumber, null);
    }

    public static TarantoolSortOperation custom(String functionDefinition, Long fieldNumber){
        return new TarantoolSortOperation(functionDefinition, fieldNumber, null);
    }


    public static TarantoolSortOperation ascending(String fieldName){
        return new TarantoolSortOperation(ASCENDING, null, fieldName);
    }

    public static TarantoolSortOperation descending(String fieldName){
        return new TarantoolSortOperation(DESCENDING, null, fieldName);
    }

    public static TarantoolSortOperation custom(String functionDefinition, String fieldName){
        return new TarantoolSortOperation(functionDefinition, null, fieldName);
    }


    public static TarantoolSortOperation ascending(TarantoolField field){
        return new TarantoolSortOperation(ASCENDING, null, field.getFieldName());
    }

    public static TarantoolSortOperation descending(TarantoolField field){
        return new TarantoolSortOperation(DESCENDING, null, field.getFieldName());
    }

    public static TarantoolSortOperation custom(String functionDefinition, TarantoolField field){
        return new TarantoolSortOperation(functionDefinition, null, field.getFieldName());
    }



    public List<Object> build(Map<String, TarantoolField> fieldMap){
        if (nonNull(fieldName)) fieldNumber = fieldMap.get(fieldName).getFieldNumber();
        return build();
    }

    private List<Object> build(){
        return linkedListOf(SORT, linkedListOf(comparatorFunction, fieldNumber));
    }
}
