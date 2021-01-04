package io.art.tarantool.model.mapping;

import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.exception.TarantoolTransactionException;
import io.art.value.immutable.Value;
import io.art.value.tuple.PlainTupleReader;
import io.art.value.tuple.schema.ValueSchema;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.RESULT_IS_INVALID;

import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.TRANSACTION_FAILED;
import static java.text.MessageFormat.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class TarantoolResponseMapping {


    public static Optional<?> toEmpty(List<?> response) {
        return empty();
    }

    public static Optional<Long> toLong(List<?> response){
        return Optional.of(((Number) response.get(0)).longValue());
    }

    public static Optional<Value> toValue(List<?> response){
        response = cast(response.get(0));
        if ((isEmpty(response.get(0))) || response.size() < 2) return empty();
        return ofNullable(readTuple(response));
    }

    public static Optional<List<Value>> toValuesList(List<?> response){
        response = cast(response.get(0));
        if (response.isEmpty()) return empty();

        List<Value> result = response.stream()
                .map(entry -> readTuple(cast(entry)))
                .collect(toList());
        return Optional.of(result);
    }

    public static Optional<Set<String>> toStringSet(List<?> response){
        List<String> indices = cast(response.get(0));
        return Optional.of(setOf(indices));
    }

    public static List<?> tupleFromTransactionResponse(List<?> transactionResponse, Integer tupleIndex){
        if (!(Boolean) transactionResponse.get(0)) throw new TarantoolTransactionException(format(TRANSACTION_FAILED, transactionResponse.get(1)));
        List<?> result = cast(transactionResponse.get(1));
        result = cast(result.get(tupleIndex));
        return result;
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
