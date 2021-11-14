package io.art.tarantool.model.record;

import io.art.core.checker.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.transaction.dependency.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;


public class TarantoolTransactionRecord<T> implements TarantoolRecord<T> {
    private final int transactionEntryNumber;
    private CompletableFuture<Optional<T>> futureResult;
    private final Function<List<?>, Optional<T>> responseMapper;
    private final List<TarantoolTransactionRecord<?>> thenApplyRecords = linkedList();

    public TarantoolTransactionRecord(int transactionEntryNumber, Function<List<?>, Optional<?>> responseMapper) {
        this.transactionEntryNumber = transactionEntryNumber;
        this.responseMapper = cast(responseMapper);
    }

    public void transactionCommitted(CompletableFuture<List<?>> futureResponse) {
        //futureResult = futureResponse;
        thenApplyRecords.forEach(record -> record.transactionCommitted(futureResponse));
    }

    @Override
    public TarantoolTransactionDependency useResult() {
        if (!isNull(futureResult)) throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
        return new TransactionTupleDependency(transactionEntryNumber);
    }

    @Override
    public TarantoolTransactionDependency useResultField(String fieldName) {
        if (!isNull(futureResult)) throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
        return new TransactionFieldDependency(transactionEntryNumber, fieldName);
    }


    public CompletableFuture<Optional<T>> getFuture() {
        if (isNull(futureResult)) throw new TarantoolTransactionException(GET_RESULT_OF_NOT_COMMITTED_TRANSACTION);
        return futureResult;
    }

    public <U> TarantoolTransactionRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper) {
        Function<List<?>, Optional<?>> newMapper = (List<?> response) -> mapper.apply(responseMapper.apply(response));
        TarantoolTransactionRecord<U> newRecord = new TarantoolTransactionRecord<U>(transactionEntryNumber, newMapper);
        thenApplyRecords.add(newRecord);
        if (!isNull(futureResult)) newRecord.futureResult = this.futureResult.thenApply(mapper);
        return newRecord;
    }

    public Optional<T> getOptional() {
        try {
            return getFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, e);
        }
    }

    public T get() {
        return getOptional().get();
    }

    public T orElse(T other) {
        return getOptional().orElse(other);
    }

    public T orElseThrow() {
        return getOptional().orElseThrow(() -> new NoSuchElementException("No emit present"));
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return getOptional().orElseThrow(exceptionSupplier);
    }

    public <U> U map(Function<Optional<T>, U> mapper) {
        return mapper.apply(getOptional());
    }


    public boolean isDone() {
        return !isNull(futureResult) && futureResult.isDone();
    }

    public boolean isPresent() {
        return EmptinessChecker.isNotEmpty(getOptional());
    }

    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(getOptional());
    }

    @Override
    public TarantoolTransactionRecord<T> synchronize() {
        getOptional();
        return this;
    }

}
