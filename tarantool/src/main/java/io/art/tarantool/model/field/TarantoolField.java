package io.art.tarantool.model.field;

import io.art.tarantool.model.operation.*;
import io.art.value.immutable.Value;
import lombok.*;

import static io.art.core.factory.ArrayFactory.fixedArrayOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectSortComparator.ASCENDING;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectSortComparator.DESCENDING;
import static io.art.tarantool.constants.TarantoolModuleConstants.TarantoolOperator.*;

@AllArgsConstructor
@Getter
public class TarantoolField {
    private final Long fieldNumber;
    private final String fieldName;

    public TarantoolField(int fieldNumber, String fieldName){
        this.fieldNumber = (long) fieldNumber;
        this.fieldName = fieldName;
    }

    public TarantoolFilterOperation is(Object value){
        return TarantoolFilterOperation.is(fieldNumber, value);
    }

    public TarantoolFilterOperation isNot(Object value){
        return TarantoolFilterOperation.isNot(fieldNumber, value);
    }

    public TarantoolFilterOperation more(Object value){
        return TarantoolFilterOperation.more(fieldNumber, value);
    }

    public TarantoolFilterOperation less(Object value){
        return TarantoolFilterOperation.less(fieldNumber, value);
    }

    public TarantoolFilterOperation inRange(Object startValue, Object endValue){
        return TarantoolFilterOperation.inRange(fieldNumber, startValue, endValue);
    }

    public TarantoolFilterOperation notInRange(Object startValue, Object endValue){
        return TarantoolFilterOperation.notInRange(fieldNumber, startValue, endValue);
    }

    public TarantoolFilterOperation like(String pattern){
        return TarantoolFilterOperation.like(fieldNumber, pattern);
    }

    public TarantoolFilterOperation startsWith(String substring){
        return TarantoolFilterOperation.startsWith(fieldNumber, substring);
    }

    public TarantoolFilterOperation endsWith(String substring){
        return TarantoolFilterOperation.endsWith(fieldNumber, substring);
    }

    public TarantoolFilterOperation contains(String substring){
        return TarantoolFilterOperation.contains(fieldNumber, substring);
    }

    public TarantoolFilterOperation customFilter(String filterFunctionDefinition, Object argument){
        return TarantoolFilterOperation.custom(filterFunctionDefinition, fieldNumber, argument);
    }



    public TarantoolSortOperation ascending(){
        return TarantoolSortOperation.ascending(fieldNumber);
    }

    public TarantoolSortOperation descending(){
        return TarantoolSortOperation.descending(fieldNumber);
    }

    public TarantoolSortOperation customSort(String functionDefinition){
        return TarantoolSortOperation.custom(functionDefinition, fieldNumber);
    }



    public TarantoolUpdateFieldOperation add(long value) {
        return TarantoolUpdateFieldOperation.addition(fieldNumber.intValue(), value);
    }

    public TarantoolUpdateFieldOperation increment() {
        return TarantoolUpdateFieldOperation.increment(fieldNumber.intValue());
    }

    public TarantoolUpdateFieldOperation subtract(long value) {
        return TarantoolUpdateFieldOperation.subtraction(fieldNumber.intValue(), value);
    }

    public TarantoolUpdateFieldOperation decrement() {
        return TarantoolUpdateFieldOperation.decrement(fieldNumber.intValue());
    }

    public TarantoolUpdateFieldOperation and(long value) {
        return TarantoolUpdateFieldOperation.and(fieldNumber.intValue(), value);
    }

    public TarantoolUpdateFieldOperation or(long value) {
        return TarantoolUpdateFieldOperation.or(fieldNumber.intValue(), value);
    }

    public TarantoolUpdateFieldOperation xor(long value) {
        return TarantoolUpdateFieldOperation.xor(fieldNumber.intValue(), value);
    }

    public TarantoolUpdateFieldOperation spliceString(long fromByte, long byteCount, String valueToInsert) {
        return TarantoolUpdateFieldOperation.stringSplice(fieldNumber.intValue(), fromByte, byteCount, valueToInsert);
    }

    public TarantoolUpdateFieldOperation insert(String fieldName, Value value) {
        return TarantoolUpdateFieldOperation.insertion(fieldNumber.intValue(), fieldName, value);
    }

    public TarantoolUpdateFieldOperation delete() {
        return TarantoolUpdateFieldOperation.deletion(fieldNumber.intValue(), 1);
    }

    public TarantoolUpdateFieldOperation assign(Value value) {
        return TarantoolUpdateFieldOperation.assigment(fieldNumber.intValue(), fieldName, value);
    }
}
