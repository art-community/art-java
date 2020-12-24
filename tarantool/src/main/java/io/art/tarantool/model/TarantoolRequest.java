package io.art.tarantool.model;

import io.art.tarantool.exception.TarantoolDaoException;
import io.art.value.immutable.Value;

import java.util.ArrayList;
import java.util.List;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.EmptinessChecker.isNotEmpty;
import static io.art.core.factory.ListFactory.linkedList;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.NULL_REQUEST_DATA_EXCEPTION;
import static io.art.value.tuple.PlainTupleWriter.*;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;


public class TarantoolRequest {

    public static List<?> requestTuple(Value request){
        PlainTupleWriterResult writerResult = writeTuple(request);
        return isNull(writerResult) ? linkedList() : writerResult.getTuple();
    }

    public static List<?> dataTuple(Value data){
        PlainTupleWriterResult writerResult = writeTuple(data);
        List<?> result = new ArrayList<>();
        if (isNull(writerResult)) throw new TarantoolDaoException(NULL_REQUEST_DATA_EXCEPTION);
        result.add(cast(writerResult.getTuple()));
        result.add(cast(writerResult.getSchema().toTuple()));
        return result;
    }

    public static List<?> updateOperationsTuple(TarantoolUpdateFieldOperation... operations) {
        List<?> valueOperations = stream(operations)
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(toList());
        List<?> schemaOperations = stream(operations)
                .filter(operation -> isNotEmpty(operation.getSchemaOperation()))
                .map(TarantoolUpdateFieldOperation::getSchemaOperation)
                .collect(toList());
        List<?> results = new ArrayList<>();
        results.add(cast(valueOperations));
        results.add(cast(schemaOperations));
        return results;
    }
}
