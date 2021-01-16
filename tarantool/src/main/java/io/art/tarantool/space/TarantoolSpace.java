package io.art.tarantool.space;

import io.art.core.collection.ImmutableArray;
import io.art.core.factory.MapFactory;
import io.art.tarantool.constants.TarantoolModuleConstants;
import io.art.tarantool.transaction.TarantoolTransactionManager;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.model.record.TarantoolRecord;
import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation;
import io.art.value.immutable.Value;
import io.art.tarantool.model.mapping.TarantoolResponseMapping;
import lombok.NonNull;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.art.core.caster.Caster.cast;
import static io.art.core.collection.ImmutableArray.immutableArrayCollector;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.model.mapping.TarantoolRequestMapping.*;

public class TarantoolSpace<T> {
    @NonNull
    private final TarantoolTransactionManager transactionManager;
    @NonNull
    private final String space;
    private final Function<Optional<Value>, Optional<T>> responseMapper;

    public TarantoolSpace(String space, TarantoolTransactionManager transactionManager, Function<Optional<Value>, Optional<T>> responseMapper){
        this.space = space;
        this.transactionManager = transactionManager;
        this.responseMapper = responseMapper;
    }


    public TarantoolRecord<T> get(Value key){
        return transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, requestTuple(key)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> get(TarantoolTransactionDependency keyDependency){
        return transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, keyDependency.get()).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> get(String index, Value key){
        return transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, index, requestTuple(key)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> get(String index, TarantoolTransactionDependency keyDependency){
        return transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, index, keyDependency.get()).thenApply(cast(responseMapper));
    }


    public SelectRequest select(Value request){
        return new SelectRequest(request);
    }

    public SelectRequest select(TarantoolTransactionDependency requestDependency){
        return new SelectRequest(requestDependency);
    }


    public TarantoolRecord<T> delete(Value key){
        return transactionManager.callRW(DELETE, TarantoolResponseMapping::toValue, space, requestTuple(key)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> delete(TarantoolTransactionDependency keyDependency){
        return transactionManager.callRW(DELETE, TarantoolResponseMapping::toValue, space, keyDependency.get()).thenApply(cast(responseMapper));
    }


    public TarantoolRecord<T> insert(Value data){
        return transactionManager.callRW(INSERT, TarantoolResponseMapping::toValue, space, dataTuple(data)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> insert(TarantoolTransactionDependency dataDependency){
        return transactionManager.callRW(INSERT, TarantoolResponseMapping::toValue, space, dataDependency.get()).thenApply(cast(responseMapper));
    }


    public TarantoolRecord<T> autoIncrement(Value data){
        return transactionManager.callRW(AUTO_INCREMENT, TarantoolResponseMapping::toValue, space, dataTuple(data)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> autoIncrement(TarantoolTransactionDependency dataDependency){
        return transactionManager.callRW(AUTO_INCREMENT, TarantoolResponseMapping::toValue, space, dataDependency.get()).thenApply(cast(responseMapper));
    }


    public TarantoolRecord<T> put(Value data){
        return transactionManager.callRW(PUT, TarantoolResponseMapping::toValue, space, dataTuple(data)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> put(TarantoolTransactionDependency dataDependency){
        return transactionManager.callRW(PUT, TarantoolResponseMapping::toValue, space, dataDependency.get()).thenApply(cast(responseMapper));
    }


    public TarantoolRecord<T> replace(Value data){
        return transactionManager.callRW(REPLACE, TarantoolResponseMapping::toValue, space, dataTuple(data)).thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> replace(TarantoolTransactionDependency dataDependency){
        return transactionManager.callRW(REPLACE, TarantoolResponseMapping::toValue, space, dataDependency.get()).thenApply(cast(responseMapper));
    }


    public TarantoolRecord<T> update(Value key, TarantoolUpdateFieldOperation... operations){
        return transactionManager.callRW(UPDATE, TarantoolResponseMapping::toValue, space, requestTuple(key), updateOperationsTuple(operations))
                .thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return transactionManager.callRW(UPDATE, TarantoolResponseMapping::toValue, space, keyDependency.get(), updateOperationsTuple(operations))
                .thenApply(cast(responseMapper));
    }


    public TarantoolRecord<T> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return transactionManager.callRW(UPSERT, TarantoolResponseMapping::toValue, space, dataTuple(defaultValue), updateOperationsTuple(operations))
                .thenApply(cast(responseMapper));
    }

    public TarantoolRecord<T> upsert(TarantoolTransactionDependency defaultValueDependency, TarantoolUpdateFieldOperation... operations){
        return transactionManager.callRW(UPSERT, TarantoolResponseMapping::toValue, space, defaultValueDependency.get(), updateOperationsTuple(operations))
                .thenApply(cast(responseMapper));
    }


    public TarantoolRecord<Long> count(){
        return cast(transactionManager.callRO(COUNT, TarantoolResponseMapping::toLong, space));
    }

    public TarantoolRecord<Long> len(){
        return cast(transactionManager.callRO(LEN, TarantoolResponseMapping::toLong, space));
    }


    public void truncate(){
        transactionManager.callRW(TRUNCATE, TarantoolResponseMapping::toEmpty, space);
    }

    public TarantoolRecord<Set<String>> listIndices(){
        return cast(transactionManager.callRO(LIST_INDICES, TarantoolResponseMapping::toStringSet, space));
    }

    public class SelectRequest {
        private final Object request;
        private final Map<String, Object> options = MapFactory.map();
        private Object index = 0;

        public SelectRequest(Value request) {
            this.request = requestTuple(request);
        }

        public SelectRequest(TarantoolTransactionDependency requestDependency) {
            this.request = requestDependency.get();
        }

        public SelectRequest index(String index) {
            this.index = index;
            return this;
        }

        public SelectRequest limit(Long limit) {
            options.put(LIMIT, limit);
            return this;
        }

        public SelectRequest offset(Long offset) {
            options.put(OFFSET, offset);
            return this;
        }

        public SelectRequest iterator(TarantoolModuleConstants.TarantoolIndexIterator iterator){
            options.put(ITERATOR, iterator.toString());
            return this;
        }

        public TarantoolRecord<ImmutableArray<T>> execute(){
            TarantoolRecord<ImmutableArray<Value>> response = cast(transactionManager
                    .callRO(SELECT, TarantoolResponseMapping::toValuesArray, space, request, index, options));
            return response.thenApply(responseData -> responseData.map(values -> values.stream()
                    .map(entry -> responseMapper.apply(Optional.of(entry)).get())
                    .collect(immutableArrayCollector())));
        }

        public ImmutableArray<T> get(){
            return execute().get();
        }

        public Stream<T> stream() {
            return execute().get().stream();
        }
    }
    
}
