package io.art.tarantool.model.operation;

import lombok.*;

import java.util.*;

import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectFilters.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.FILTER;
import static java.util.Optional.empty;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TarantoolFilterOperation {
    private final String filterFunction;
    private final Long fieldNumber;
    private final Object firstArgument;
    private final Optional<Object> secondArgument;

    public static TarantoolFilterOperation equals(Long fieldNumber, Object value){
        return new TarantoolFilterOperation(EQUALS, fieldNumber, value, empty());
    }

    public static TarantoolFilterOperation notEquals(Long fieldNumber, Object value){
        return new TarantoolFilterOperation(NOT_EQUALS, fieldNumber, value, empty());
    }

    public static TarantoolFilterOperation more(Long fieldNumber, Object value){
        return new TarantoolFilterOperation(MORE, fieldNumber, value, empty());
    }

    public static TarantoolFilterOperation less(Long fieldNumber, Object value){
        return new TarantoolFilterOperation(LESS, fieldNumber, value, empty());
    }

    public static TarantoolFilterOperation inRange(Long fieldNumber, Object startValue, Object endValue){
        return new TarantoolFilterOperation(IN_RANGE, fieldNumber, startValue, Optional.of(endValue));
    }

    public static TarantoolFilterOperation notInRange(Long fieldNumber, Object startValue, Object endValue){
        return new TarantoolFilterOperation(MOT_IN_RANGE, fieldNumber, startValue, Optional.of(endValue));
    }

    public static TarantoolFilterOperation like(Long fieldNumber, String pattern){
        return new TarantoolFilterOperation(LIKE, fieldNumber, pattern, empty());
    }

    public static TarantoolFilterOperation startsWith(Long fieldNumber, String substring){
        return new TarantoolFilterOperation(STARTS_WITH, fieldNumber, substring, empty());
    }

    public static TarantoolFilterOperation endsWith(Long fieldNumber, String substring){
        return new TarantoolFilterOperation(ENDS_WITH, fieldNumber, substring, empty());
    }

    public static TarantoolFilterOperation contains(Long fieldNumber, String substring){
        return new TarantoolFilterOperation(CONTAINS, fieldNumber, substring, empty());
    }

    public static TarantoolFilterOperation custom(String filterFunctionDefinition, Long fieldNumber, Object argument){
        return new TarantoolFilterOperation(filterFunctionDefinition, fieldNumber, argument, empty());
    }

    public List<Object> build(){
        return secondArgument
                .map(o -> linkedListOf(FILTER, linkedListOf(filterFunction, fieldNumber, firstArgument, o)))
                .orElseGet(() -> linkedListOf(FILTER, linkedListOf(filterFunction, fieldNumber, firstArgument)));
    }
}
