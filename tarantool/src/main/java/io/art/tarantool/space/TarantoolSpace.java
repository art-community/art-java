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
import static io.art.tarantool.model.mapping.TarantoolResponseMapping.toValue;
import static io.art.tarantool.model.mapping.TarantoolResponseMapping.toValuesArray;

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
        return cast(transactionManager.callRO(GET, response -> responseMapper.apply(toValue(response)), space, requestTuple(key)));
    }

    public TarantoolRecord<T> get(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRO(GET, response -> responseMapper.apply(toValue(response)), space, keyDependency.get()));
    }

    public TarantoolRecord<T> get(String index, Value key){
        return cast(transactionManager.callRO(GET, response -> responseMapper.apply(toValue(response)), space, index, requestTuple(key)));
    }

    public TarantoolRecord<T> get(String index, TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRO(GET, response -> responseMapper.apply(toValue(response)), space, index, keyDependency.get()));
    }


    public SelectRequest select(Value request){
        return new SelectRequest(request);
    }

    public SelectRequest select(TarantoolTransactionDependency requestDependency){
        return new SelectRequest(requestDependency);
    }


    public TarantoolRecord<T> delete(Value key){
        return cast(transactionManager.callRW(DELETE, response -> responseMapper.apply(toValue(response)), space, requestTuple(key)));
    }

    public TarantoolRecord<T> delete(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRW(DELETE, response -> responseMapper.apply(toValue(response)), space, keyDependency.get()));
    }


    public TarantoolRecord<T> insert(Value data){
        return cast(transactionManager.callRW(INSERT, response -> responseMapper.apply(toValue(response)), space, dataTuple(data)));
    }

    public TarantoolRecord<T> insert(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(INSERT, response -> responseMapper.apply(toValue(response)), space, dataDependency.get()));
    }


    public TarantoolRecord<T> autoIncrement(Value data){
        return cast(transactionManager.callRW(AUTO_INCREMENT, response -> responseMapper.apply(toValue(response)), space, dataTuple(data)));
    }

    public TarantoolRecord<T> autoIncrement(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(AUTO_INCREMENT, response -> responseMapper.apply(toValue(response)), space, dataDependency.get()));
    }


    public TarantoolRecord<T> put(Value data){
        return cast(transactionManager.callRW(PUT, response -> responseMapper.apply(toValue(response)), space, dataTuple(data)));
    }

    public TarantoolRecord<T> put(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(PUT, response -> responseMapper.apply(toValue(response)), space, dataDependency.get()));
    }


    public TarantoolRecord<T> replace(Value data){
        return cast(transactionManager.callRW(REPLACE, response -> responseMapper.apply(toValue(response)), space, dataTuple(data)));
    }

    public TarantoolRecord<T> replace(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(REPLACE, response -> responseMapper.apply(toValue(response)), space, dataDependency.get()));
    }


    public TarantoolRecord<T> update(Value key, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPDATE, response -> responseMapper.apply(toValue(response)), space,
                requestTuple(key), updateOperationsTuple(operations)));
    }

    public TarantoolRecord<T> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPDATE, response -> responseMapper.apply(toValue(response)), space,
                keyDependency.get(), updateOperationsTuple(operations)));
    }


    public TarantoolRecord<T> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPSERT, response -> responseMapper.apply(toValue(response)), space,
                dataTuple(defaultValue), updateOperationsTuple(operations)));
    }

    public TarantoolRecord<T> upsert(TarantoolTransactionDependency defaultValueDependency, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPSERT, response -> responseMapper.apply(toValue(response)), space,
                defaultValueDependency.get(), updateOperationsTuple(operations)));
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
            Function<List<?>, Optional<ImmutableArray<T>>> mapper =
                    response -> toValuesArray(response).map(values -> values.stream()
                    .map(entry -> responseMapper.apply(Optional.of(entry)).get())
                    .collect(immutableArrayCollector()));
            return cast(transactionManager.callRO(SELECT, cast(mapper), space, request, index, options));
        }

        public ImmutableArray<T> get(){
            return execute().get();
        }

        public Stream<T> stream() {
            return execute().get().stream();
        }
    }
    
}
