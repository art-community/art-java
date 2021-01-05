package io.art.tarantool.model;

import io.art.core.checker.*;
import io.art.value.immutable.*;
import io.art.value.tuple.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.value.tuple.PlainTupleWriter.*;
import static java.util.Arrays.*;
import java.util.ArrayList;
import java.util.*;

public class TarantoolRequest {

    public static List<?> requestTuple(Value request) {
        return writeTuple(request).getTuple();
    }

    public static List<?> dataTuple(Value data) {
        PlainTupleWriter.PlainTupleWriterResult writerResult = writeTuple(data);
        List<?> result = new ArrayList<>();
        result.add(cast(writerResult.getTuple()));
        result.add(cast(writerResult.getSchema().toTuple()));
        return result;
    }

    public static List<?> updateOperationsTuple(TarantoolUpdateFieldOperation... operations) {
        List<?> valueOperations = stream(operations)
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(arrayCollector());
        List<?> schemaOperations = stream(operations)
                .map(TarantoolUpdateFieldOperation::getSchemaOperation)
                .filter(EmptinessChecker::isNotEmpty)
                .collect(arrayCollector());
        List<?> results = new ArrayList<>();
        results.add(cast(valueOperations));
        results.add(cast(schemaOperations));
        return results;
    }
}
