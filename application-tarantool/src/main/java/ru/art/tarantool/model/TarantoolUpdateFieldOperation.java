package ru.art.tarantool.model;

import lombok.Getter;
import ru.art.entity.Value;
import ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolOperator;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.tuple.PlainTupleWriter.PlainTupleWriterResult;
import static ru.art.entity.tuple.PlainTupleWriter.writeTuple;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolOperator.*;
import java.util.List;

@Getter
public class TarantoolUpdateFieldOperation {
    private List<?> valueOperation;
    private List<?> schemaOperation = emptyList();

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, int fieldCount) {
        valueOperation = fixedArrayOf(operator.getOperator(), fieldNumber, fieldCount);
        schemaOperation = fixedArrayOf(operator.getOperator(), fieldNumber + 2, fieldCount);
    }

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, Object value) {
        valueOperation = fixedArrayOf(operator.getOperator(), fieldNumber, value);
    }

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, List<?> value) {
        valueOperation = dynamicArrayOf(operator.getOperator(), fieldNumber);
        valueOperation.addAll(cast(value));
    }

    private TarantoolUpdateFieldOperation(TarantoolOperator operator, int fieldNumber, String fieldName, Value value) {
        PlainTupleWriterResult result = writeTuple(entityBuilder().valueField(fieldName, value).build());
        if (isNull(result)) {
            valueOperation = emptyList();
            return;
        }
        switch (operator) {
            case INSERTION:
            case ASSIGMENT:
                schemaOperation = fixedArrayOf(operator.getOperator(), fieldNumber + 2, result.getSchema().toTuple().get(1));
                break;
        }
        valueOperation = fixedArrayOf(operator.getOperator(), fieldNumber, result.getTuple().get(0));
    }

    public static TarantoolUpdateFieldOperation addition(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(ADDITION, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation subtraction(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(SUBTRACTION, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation and(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(AND, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation or(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(OR, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation xor(int fieldNumber, long value) {
        return new TarantoolUpdateFieldOperation(XOR, fieldNumber, value);
    }

    public static TarantoolUpdateFieldOperation stringSplice(int fieldNumber, long fromByte, long byteCount, String valueToInsert) {
        return new TarantoolUpdateFieldOperation(STRING_SPLICE, fieldNumber, fixedArrayOf(fromByte, byteCount, valueToInsert));
    }

    public static TarantoolUpdateFieldOperation insertion(int fieldNumber, String fieldName, Value value) {
        return new TarantoolUpdateFieldOperation(INSERTION, fieldNumber, fieldName, value);
    }

    public static TarantoolUpdateFieldOperation deletion(int fieldNumber, int fieldCount) {
        return new TarantoolUpdateFieldOperation(DELETION, fieldNumber, fieldCount);
    }

    public static TarantoolUpdateFieldOperation assigment(int fieldNumber, String fieldName, Value value) {
        return new TarantoolUpdateFieldOperation(ASSIGMENT, fieldNumber, fieldName, value);
    }
}
