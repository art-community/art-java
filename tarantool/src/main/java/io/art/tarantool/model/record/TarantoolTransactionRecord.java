package io.art.tarantool.model.record;

import io.art.core.checker.EmptinessChecker;

import io.art.storage.record.StorageRecord;
import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.model.transaction.dependency.TransactionFieldDependency;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.model.transaction.dependency.TransactionTupleDependency;
import io.art.tarantool.exception.TarantoolTransactionException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.ListFactory.linkedList;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static io.art.tarantool.model.mapping.TarantoolResponseMapping.tupleFromTransaction;
import static java.util.Objects.isNull;


public class TarantoolTransactionRecord<T> implements TarantoolRecord<T> {
    private final int transactionEntryNumber;
    private CompletableFuture<Optional<T>> futureResult;
    private final Function<List<?>, Optional<T>> responseMapper;
    private final List<TarantoolTransactionRecord<?>> thenApplyRecords = linkedList();

    public TarantoolTransactionRecord(int transactionEntryNumber, Function<List<?>, Optional<?>> responseMapper){
        this.transactionEntryNumber = transactionEntryNumber;
        this.responseMapper = cast(responseMapper);
    }

    public void transactionCommitted(CompletableFuture<List<?>> futureResponse) {
        futureResult = futureResponse
                .thenApply(response -> responseMapper.apply(tupleFromTransaction(response, transactionEntryNumber)));
        thenApplyRecords.forEach(record -> record.transactionCommitted(futureResponse));
    }

    @Override
    public TarantoolTransactionDependency useResult(){
        if (!isNull(futureResult)) throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
        return new TransactionTupleDependency(transactionEntryNumber);
    }

    @Override
    public TarantoolTransactionDependency useResultField(String fieldName){
        if (!isNull(futureResult)) throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
        return new TransactionFieldDependency(transactionEntryNumber, fieldName);
    }



    @Override
    public CompletableFuture<Optional<T>> getFuture() {
        if (isNull(futureResult)) throw new TarantoolTransactionException(GET_RESULT_OF_NOT_COMMITTED_TRANSACTION);
        return futureResult;
    }

    @Override
    public <U> TarantoolTransactionRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper){
        Function<List<?>, Optional<?>> newMapper = (List<?> response) -> mapper.apply(responseMapper.apply(response));
        TarantoolTransactionRecord<U> newRecord = new TarantoolTransactionRecord<U>(transactionEntryNumber, newMapper);
        thenApplyRecords.add(newRecord);
        if (!isNull(futureResult)) newRecord.futureResult = this.futureResult.thenApply(mapper);
        return newRecord;
    }

    @Override
    public Optional<T> getOptional(){
        try {
            return getFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, e);
        }
    }

    @Override
    public T get() {
        return getOptional().get();
    }

    @Override
    public T orElse(T other) {
        return getOptional().orElse(other);
    }

    @Override
    public T orElseThrow() {
        return getOptional().orElseThrow(() -> new NoSuchElementException("No value present"));
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return getOptional().orElseThrow(exceptionSupplier);
    }

    @Override
    public <U> U map(Function<Optional<T>, U> mapper) {
        return mapper.apply(getOptional());
    }



    @Override
    public boolean isDone() {
        return !isNull(futureResult) && futureResult.isDone();
    }

    @Override
    public boolean isPresent() {
        return EmptinessChecker.isNotEmpty(getOptional());
    }

    @Override
    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(getOptional());
    }

    @Override
    public TarantoolTransactionRecord<T> synchronize(){
        getOptional();
        return this;
    }

}
