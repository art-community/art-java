package io.art.tarantool.model;

import io.art.value.immutable.Value;
import io.art.value.tuple.PlainTupleWriter;

import java.util.ArrayList;
import java.util.List;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.EmptinessChecker.isNotEmpty;
import static io.art.value.tuple.PlainTupleWriter.writeTuple;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class TarantoolRequest {

    public static List<?> requestTuple(Value request){
        return writeTuple(request).getTuple();
    }

    public static List<?> dataTuple(Value data){
        PlainTupleWriter.PlainTupleWriterResult writerResult = writeTuple(data);
        List<?> result = new ArrayList<>();
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
