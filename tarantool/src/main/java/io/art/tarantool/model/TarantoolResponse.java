package io.art.tarantool.model;

import io.art.tarantool.exception.TarantoolDaoException;
import io.art.value.immutable.Value;
import io.art.value.tuple.PlainTupleReader;
import io.art.value.tuple.schema.ValueSchema;

import java.util.List;
import java.util.Optional;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.RESULT_IS_INVALID;

import static java.text.MessageFormat.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class TarantoolResponse {

    public static Optional<Value> read(List<?> response){
        response = cast(response.get(0));
        if ((isEmpty(response.get(0))) || response.size() < 2) return empty();
        return ofNullable(readTuple(response));
    }

    public static Optional<List<io.art.value.immutable.Value>> readBatch(List<?> response){
        response = cast(response.get(0));
        if (response.isEmpty()) return empty();

        List<io.art.value.immutable.Value> result = response.stream()
                .map(entry -> readTuple(cast(entry)))
                .collect(toList());
        return ofNullable(result);
    }

    private static Value readTuple(List<?> tuple){
        try {
            List<?> data = cast(tuple.get(0));
            ValueSchema schema = ValueSchema.fromTuple(cast(tuple.get(1)));
            return PlainTupleReader.readTuple(data, schema);
        } catch(Throwable throwable){
            throw new TarantoolDaoException(format(RESULT_IS_INVALID, tuple.toString()), throwable);
        }
    }
}
