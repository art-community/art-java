package io.art.tarantool.model.mapping;

public class TarantoolResponseMapping {


/*
    public static Optional<?> toEmpty(List<?> response) {
        return empty();
    }

    public static Optional<Long> toLong(List<?> response) {
        return Optional.of(((Number) response.get(0)).longValue());
    }

    public static Optional<Value> toValue(List<?> response) {
        response = cast(response.get(0));
        if ((isEmpty(response.get(0))) || response.size() < 2) return empty();
        return ofNullable(readTuple(response));
    }

    public static Optional<ImmutableArray<Value>> toValuesArray(List<?> response) {
        response = cast(response.get(0));
        if (response.isEmpty()) return Optional.of(ImmutableArray.emptyImmutableArray());

        ImmutableArray<Value> result = response.stream()
                .map(entry -> readTuple(cast(entry)))
                .collect(immutableArrayCollector());
        return Optional.of(result);
    }

    public static Optional<Set<String>> toStringSet(List<?> response) {
        List<String> indices = cast(response.get(0));
        return Optional.of(setOf(indices));
    }

    public static List<?> tupleFromTransaction(List<?> transactionResponse, Integer tupleIndex) {
        if (!(Boolean) transactionResponse.get(0))
            throw new TarantoolTransactionException(format(TRANSACTION_FAILED, transactionResponse.get(1)));
        List<?> result = cast(transactionResponse.get(1));
        result = cast(result.get(tupleIndex));
        return result;
    }

    private static Value readTuple(List<?> tuple) {
        try {
            List<?> data = cast(tuple.get(0));
            ValueSchema schema = ValueSchema.fromTuple(cast(tuple.get(1)));
            return PlainTupleReader.readTuple(data, schema);
        } catch (Throwable throwable) {
            throw new TarantoolDaoException(format(RESULT_IS_INVALID, tuple.toString()), throwable);
        }
    }
*/
}
