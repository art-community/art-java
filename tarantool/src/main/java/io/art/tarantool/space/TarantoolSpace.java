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
import io.art.storage.space.Space;
import lombok.Builder;
import lombok.Setter;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.art.core.caster.Caster.cast;
import static io.art.core.collection.ImmutableArray.immutableArrayCollector;
import static io.art.core.constants.EmptyFunctions.emptyFunction;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.model.mapping.TarantoolRequestMapping.*;
import static io.art.tarantool.model.mapping.TarantoolResponseMapping.toValue;
import static io.art.tarantool.model.mapping.TarantoolResponseMapping.toValuesArray;

public class TarantoolSpace<T, K> implements Space<T, K> {
    private final TarantoolTransactionManager transactionManager;
    private final String space;
    private final Function<Optional<Value>, Optional<T>> toModelMapper;
    private final Function<T, Value> fromModelMapper;
    private final Function<K, Value> keyMapper;
    private final Function<List<?>, Optional<?>> selectToModelMapper;
    @Setter
    private Function<T, Long> bucketIdGenerator = emptyFunction();

    @Builder
    public TarantoolSpace(String space,
                          TarantoolTransactionManager transactionManager,
                          Function<Optional<Value>, Optional<T>> toModelMapper,
                          Function<T, Value> fromModelMapper,
                          Function<K, Value> keyMapper){
        this.space = space;
        this.transactionManager = transactionManager;
        this.toModelMapper = toModelMapper;
        this.fromModelMapper = fromModelMapper;
        this.keyMapper = keyMapper;
        selectToModelMapper = response -> toValuesArray(response).map(values -> values.stream()
                        .map(entry -> toModelMapper.apply(Optional.of(entry)).get())
                        .collect(immutableArrayCollector()));
    }


    public TarantoolRecord<T> get(K key){
        return cast(transactionManager.callRO(GET, response -> toModelMapper.apply(toValue(response)), space,
                requestTuple(keyMapper.apply(key))));
    }

    public TarantoolRecord<T> get(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRO(GET, response -> toModelMapper.apply(toValue(response)), space, keyDependency.get()));
    }

    public TarantoolRecord<T> get(String index, Value key){
        return cast(transactionManager.callRO(GET, response -> toModelMapper.apply(toValue(response)), space, index,
                requestTuple(key)));
    }

    public TarantoolRecord<T> get(String index, TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRO(GET, response -> toModelMapper.apply(toValue(response)), space, index, keyDependency.get()));
    }


    public TarantoolRecord<ImmutableArray<T>> getAll(){
        return cast(transactionManager.callRO(SELECT, selectToModelMapper, space));
    }


    public SelectRequest select(Value request){
        return new SelectRequest(request);
    }

    public SelectRequest select(TarantoolTransactionDependency requestDependency){
        return new SelectRequest(requestDependency);
    }


    public TarantoolRecord<T> delete(K key){
        return cast(transactionManager.callRW(DELETE, response -> toModelMapper.apply(toValue(response)), space,
                requestTuple(keyMapper.apply(key))));
    }

    public TarantoolRecord<T> delete(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRW(DELETE, response -> toModelMapper.apply(toValue(response)), space, keyDependency.get()));
    }


    public TarantoolRecord<T> insert(T data){
        return cast(transactionManager.callRW(INSERT, response -> toModelMapper.apply(toValue(response)), space,
                dataTuple(fromModelMapper.apply(data)), bucketIdGenerator.apply(data)));
    }

    public TarantoolRecord<T> insert(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(INSERT, response -> toModelMapper.apply(toValue(response)), space,
                dataDependency.get()));
    }


    public TarantoolRecord<T> put(T data){
        return cast(transactionManager.callRW(PUT, response -> toModelMapper.apply(toValue(response)), space,
                dataTuple(fromModelMapper.apply(data)), bucketIdGenerator.apply(data)));
    }

    public TarantoolRecord<T> put(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(PUT, response -> toModelMapper.apply(toValue(response)), space,
                dataDependency.get()));
    }


    public TarantoolRecord<T> replace(T data){
        return cast(transactionManager.callRW(REPLACE, response -> toModelMapper.apply(toValue(response)), space,
                dataTuple(fromModelMapper.apply(data)), bucketIdGenerator.apply(data)));
    }

    public TarantoolRecord<T> replace(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(REPLACE, response -> toModelMapper.apply(toValue(response)), space,
                dataDependency.get()));
    }


    public TarantoolRecord<T> update(K key, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPDATE, response -> toModelMapper.apply(toValue(response)), space,
                requestTuple(keyMapper.apply(key)), updateOperationsTuple(operations)));
    }

    public TarantoolRecord<T> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPDATE, response -> toModelMapper.apply(toValue(response)), space,
                keyDependency.get(), updateOperationsTuple(operations)));
    }


    public TarantoolRecord<T> upsert(T defaultData, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPSERT, response -> toModelMapper.apply(toValue(response)), space,
                dataTuple(fromModelMapper.apply(defaultData)), updateOperationsTuple(operations), bucketIdGenerator.apply(defaultData)));
    }

    public TarantoolRecord<T> upsert(TarantoolTransactionDependency defaultDataDependency, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPSERT, response -> toModelMapper.apply(toValue(response)), space,
                defaultDataDependency.get(), updateOperationsTuple(operations)));
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

    public Long bucketOf(T data){
        return bucketIdGenerator.apply(data);
    }

    public void beginTransaction(){
        transactionManager.begin();
    }

    public void beginTransaction(Long bucketId){
        transactionManager.begin(bucketId);
    }

    public void commitTransaction(){
        transactionManager.commit();
    }

    public void cancelTransaction(){
        transactionManager.cancel();
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
            return cast(transactionManager.callRO(SELECT, selectToModelMapper, space, request, index, options));
        }

        public ImmutableArray<T> get(){
            return execute().get();
        }

        public Stream<T> stream() {
            return execute().get().stream();
        }
    }

}
