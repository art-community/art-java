package io.art.tarantool.model.operation;

import io.art.tarantool.model.field.*;
import lombok.*;

import java.util.*;

import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectFilters.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.FILTER;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TarantoolFilterOperation {
    private final String filterFunction;
    private Long fieldNumber;
    private final Object firstArgument;
    private final Optional<Object> secondArgument;
    private final String fieldName;

    public static TarantoolFilterOperation is(Long fieldNumber, Object equalsTo){
        return new TarantoolFilterOperation(EQUALS, fieldNumber, equalsTo, empty(), null);
    }

    public static TarantoolFilterOperation isNot(Long fieldNumber, Object notEqualsTo){
        return new TarantoolFilterOperation(NOT_EQUALS, fieldNumber, notEqualsTo, empty(), null);
    }

    public static TarantoolFilterOperation more(Long fieldNumber, Object value){
        return new TarantoolFilterOperation(MORE, fieldNumber, value, empty(), null);
    }

    public static TarantoolFilterOperation less(Long fieldNumber, Object value){
        return new TarantoolFilterOperation(LESS, fieldNumber, value, empty(), null);
    }

    public static TarantoolFilterOperation inRange(Long fieldNumber, Object startValue, Object endValue){
        return new TarantoolFilterOperation(IN_RANGE, fieldNumber, startValue, Optional.of(endValue), null);
    }

    public static TarantoolFilterOperation notInRange(Long fieldNumber, Object startValue, Object endValue){
        return new TarantoolFilterOperation(MOT_IN_RANGE, fieldNumber, startValue, Optional.of(endValue), null);
    }

    public static TarantoolFilterOperation like(Long fieldNumber, String pattern){
        return new TarantoolFilterOperation(LIKE, fieldNumber, pattern, empty(), null);
    }

    public static TarantoolFilterOperation startsWith(Long fieldNumber, String substring){
        return new TarantoolFilterOperation(STARTS_WITH, fieldNumber, substring, empty(), null);
    }

    public static TarantoolFilterOperation endsWith(Long fieldNumber, String substring){
        return new TarantoolFilterOperation(ENDS_WITH, fieldNumber, substring, empty(), null);
    }

    public static TarantoolFilterOperation contains(Long fieldNumber, String substring){
        return new TarantoolFilterOperation(CONTAINS, fieldNumber, substring, empty(), null);
    }

    public static TarantoolFilterOperation custom(String filterFunctionDefinition, Long fieldNumber, Object argument){
        return new TarantoolFilterOperation(filterFunctionDefinition, fieldNumber, argument, empty(), null);
    }



    public static TarantoolFilterOperation is(String fieldName, Object equalsTo){
        return new TarantoolFilterOperation(EQUALS, null, equalsTo, empty(), fieldName);
    }

    public static TarantoolFilterOperation isNot(String fieldName, Object notEqualsTo){
        return new TarantoolFilterOperation(NOT_EQUALS, null, notEqualsTo, empty(), fieldName);
    }

    public static TarantoolFilterOperation more(String fieldName, Object value){
        return new TarantoolFilterOperation(MORE, null, value, empty(), fieldName);
    }

    public static TarantoolFilterOperation less(String fieldName, Object value){
        return new TarantoolFilterOperation(LESS, null, value, empty(), fieldName);
    }

    public static TarantoolFilterOperation inRange(String fieldName, Object startValue, Object endValue){
        return new TarantoolFilterOperation(IN_RANGE, null, startValue, Optional.of(endValue), fieldName);
    }

    public static TarantoolFilterOperation notInRange(String fieldName, Object startValue, Object endValue){
        return new TarantoolFilterOperation(MOT_IN_RANGE, null, startValue, Optional.of(endValue), fieldName);
    }

    public static TarantoolFilterOperation like(String fieldName, String pattern){
        return new TarantoolFilterOperation(LIKE, null, pattern, empty(), fieldName);
    }

    public static TarantoolFilterOperation startsWith(String fieldName, String substring){
        return new TarantoolFilterOperation(STARTS_WITH, null, substring, empty(), fieldName);
    }

    public static TarantoolFilterOperation endsWith(String fieldName, String substring){
        return new TarantoolFilterOperation(ENDS_WITH, null, substring, empty(), fieldName);
    }

    public static TarantoolFilterOperation contains(String fieldName, String substring){
        return new TarantoolFilterOperation(CONTAINS, null, substring, empty(), fieldName);
    }

    public static TarantoolFilterOperation custom(String filterFunctionDefinition, String fieldName, Object argument){
        return new TarantoolFilterOperation(filterFunctionDefinition, null, argument, empty(), fieldName);
    }



    public static TarantoolFilterOperation is(TarantoolField field, Object equalsTo){
        return new TarantoolFilterOperation(EQUALS, null, equalsTo, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation isNot(TarantoolField field, Object notEqualsTo){
        return new TarantoolFilterOperation(NOT_EQUALS, null, notEqualsTo, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation more(TarantoolField field, Object value){
        return new TarantoolFilterOperation(MORE, null, value, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation less(TarantoolField field, Object value){
        return new TarantoolFilterOperation(LESS, null, value, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation inRange(TarantoolField field, Object startValue, Object endValue){
        return new TarantoolFilterOperation(IN_RANGE, null, startValue, Optional.of(endValue), field.getFieldName());
    }

    public static TarantoolFilterOperation notInRange(TarantoolField field, Object startValue, Object endValue){
        return new TarantoolFilterOperation(MOT_IN_RANGE, null, startValue, Optional.of(endValue), field.getFieldName());
    }

    public static TarantoolFilterOperation like(TarantoolField field, String pattern){
        return new TarantoolFilterOperation(LIKE, null, pattern, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation startsWith(TarantoolField field, String substring){
        return new TarantoolFilterOperation(STARTS_WITH, null, substring, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation endsWith(TarantoolField field, String substring){
        return new TarantoolFilterOperation(ENDS_WITH, null, substring, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation contains(TarantoolField field, String substring){
        return new TarantoolFilterOperation(CONTAINS, null, substring, empty(), field.getFieldName());
    }

    public static TarantoolFilterOperation custom(String filterFunctionDefinition, TarantoolField field, Object argument){
        return new TarantoolFilterOperation(filterFunctionDefinition, null, argument, empty(), field.getFieldName());
    }



    public List<Object> build(Map<String, TarantoolField> fieldMap){
        if (nonNull(fieldName)) fieldNumber = fieldMap.get(fieldName).getFieldNumber();
        return build();
    }

    private List<Object> build(){
        return secondArgument
                .map(secondArgument -> linkedListOf(FILTER, linkedListOf(filterFunction, fieldNumber, firstArgument, secondArgument)))
                .orElseGet(() -> linkedListOf(FILTER, linkedListOf(filterFunction, fieldNumber, firstArgument)));
    }
}
