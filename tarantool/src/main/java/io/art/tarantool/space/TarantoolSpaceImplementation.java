package io.art.tarantool.space;

import io.art.core.collection.*;
import io.art.tarantool.model.mapping.*;
import io.art.tarantool.model.operation.*;
import io.art.tarantool.model.record.*;
import io.art.tarantool.model.transaction.dependency.*;
import io.art.tarantool.transaction.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.Builder;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.model.mapping.TarantoolRequestMapping.*;
import static io.art.tarantool.model.mapping.TarantoolResponseMapping.*;

public class TarantoolSpaceImplementation<T, K> implements TarantoolSpace<T, K> {
    private final TarantoolTransactionManager transactionManager;
    private final String space;
    private final Function<Optional<Value>, Optional<T>> toModelMapper;
    private final Function<T, Value> fromModelMapper;
    private final Function<K, Value> keyMapper;
    private final Function<List<?>, Optional<?>> selectToModelMapper;
    private Function<T, Long> bucketIdGenerator = emptyFunction();

    @Builder
    public TarantoolSpaceImplementation(String space,
                          TarantoolTransactionManager transactionManager,
                          ValueToModelMapper<T, ? extends Value> toModelMapper,
                          ValueFromModelMapper<T, ? extends Value> fromModelMapper,
                          ValueFromModelMapper<K, ? extends Value> keyMapper){
        this.space = space;
        this.transactionManager = transactionManager;
        this.toModelMapper = (optional) -> optional.map((value) -> toModelMapper.map(cast(value)));
        this.fromModelMapper = fromModelMapper::map;
        this.keyMapper = keyMapper::map;
        selectToModelMapper = response -> toValuesArray(response).map(values -> values.stream()
                .map(entry -> this.toModelMapper.apply(Optional.of(entry)).get())
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


    public SelectRequest select(K request){
        return new SelectRequest(keyMapper.apply(request));
    }

    public SelectRequest select(TarantoolTransactionDependency requestDependency){
        return new SelectRequest(requestDependency);
    }

    public SelectRequest select(String index, Value request){
        return new SelectRequest(request).index(index);
    }

    public SelectRequest select(String index, TarantoolTransactionDependency requestDependency){
        return new SelectRequest(requestDependency).index(index);
    }


    public TarantoolRecord<T> delete(K key){
        return cast(transactionManager.callRW(DELETE, response -> toModelMapper.apply(toValue(response)), space,
                requestTuple(keyMapper.apply(key))));
    }

    public TarantoolRecord<T> delete(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRW(DELETE, response -> toModelMapper.apply(toValue(response)), space, keyDependency.get()));
    }


    public TarantoolRecord<T> insert(T data){
        return cast(transactionManager.callRW(INSERT, response -> toModelMapper.apply(toValue(response)),
                space, dataTuple(fromModelMapper.apply(data)), bucketIdGenerator.apply(data)));
    }

    public TarantoolRecord<T> insert(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(INSERT, response -> toModelMapper.apply(toValue(response)),
                space, dataDependency.get()));
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

    public void bucketIdGenerator(Function<T, Long> generator){
        this.bucketIdGenerator = generator;
    }

    public class SelectRequest {
        private final Object request;
        private Object index = 0;
        String iterator = TarantoolIndexIterator.EQ.toString();
        private final List<List<Object>> stream = linkedList();


        public SelectRequest(Value request) {
            this.request = requestTuple(request);
        }

        public SelectRequest(TarantoolTransactionDependency requestDependency) {
            this.request = requestDependency.get();
        }

        private  SelectRequest index(String index) {
            this.index = index;
            return this;
        }

        public SelectRequest limit(Long limit) {
            stream.add(linkedListOf(LIMIT, limit));
            return this;
        }

        public SelectRequest offset(Long offset) {
            stream.add(linkedListOf(OFFSET, offset));
            return this;
        }

//        public SelectRequest filterBy(Long field, Object value, String comparator){
//            filters.add(cast(linkedListOf(field, value, comparator)));
//            return this;
//        }
//
//        public SelectRequest sortBy(Long field, String comparator){
//            sortMethod = linkedListOf(field, comparator);
//            return this;
//        }

        public SelectRequest iterator(TarantoolIndexIterator iterator){
            this.iterator = iterator.toString();
            return this;
        }

        public TarantoolRecord<ImmutableArray<T>> execute(){
            return cast(transactionManager.callRO(SELECT, selectToModelMapper, space, request, index, iterator, stream));
        }

        public ImmutableArray<T> get(){
            return execute().get();
        }

        public Stream<T> stream() {
            return execute().get().stream();
        }
    }

}
