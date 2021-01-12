package io.art.tarantool.space;

import io.art.core.factory.MapFactory;
import io.art.tarantool.constants.TarantoolModuleConstants;
import io.art.tarantool.transaction.TarantoolTransactionManager;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.model.record.TarantoolRecord;
import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation;
import io.art.value.immutable.Value;
import io.art.tarantool.model.mapping.TarantoolResponseMapping;
import lombok.AllArgsConstructor;
import lombok.NonNull;


import java.util.*;
import java.util.stream.Stream;

import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.model.mapping.TarantoolRequestMapping.*;

@AllArgsConstructor
public class TarantoolSpace {
    @NonNull
    private final TarantoolTransactionManager transactionManager;
    @NonNull
    private final String space;


    public TarantoolRecord<Value> get(Value key){
        return cast(transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, requestTuple(key)));
    }

    public TarantoolRecord<Value> get(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, keyDependency.get()));
    }

    public TarantoolRecord<Value> get(String index, Value key){
        return cast(transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, index, requestTuple(key)));
    }

    public TarantoolRecord<Value> get(String index, TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRO(GET, TarantoolResponseMapping::toValue, space, index, keyDependency.get()));
    }


    public SelectRequest select(Value request){
        return new SelectRequest(request);
    }

    public SelectRequest select(TarantoolTransactionDependency requestDependency){
        return new SelectRequest(requestDependency);
    }


    public TarantoolRecord<Value> delete(Value key){
        return cast(transactionManager.callRW(DELETE, TarantoolResponseMapping::toValue, space, requestTuple(key)));
    }

    public TarantoolRecord<Value> delete(TarantoolTransactionDependency keyDependency){
        return cast(transactionManager.callRW(DELETE, TarantoolResponseMapping::toValue, space, keyDependency.get()));
    }


    public TarantoolRecord<Value> insert(Value data){
        return cast(transactionManager.callRW(INSERT, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolRecord<Value> insert(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(INSERT, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolRecord<Value> autoIncrement(Value data){
        return cast(transactionManager.callRW(AUTO_INCREMENT, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolRecord<Value> autoIncrement(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(AUTO_INCREMENT, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolRecord<Value> put(Value data){
        return cast(transactionManager.callRW(PUT, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolRecord<Value> put(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(PUT, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolRecord<Value> replace(Value data){
        return cast(transactionManager.callRW(REPLACE, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolRecord<Value> replace(TarantoolTransactionDependency dataDependency){
        return cast(transactionManager.callRW(REPLACE, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolRecord<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPDATE, TarantoolResponseMapping::toValue, space, requestTuple(key), updateOperationsTuple(operations)));
    }

    public TarantoolRecord<Value> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPDATE, TarantoolResponseMapping::toValue, space, keyDependency.get(), updateOperationsTuple(operations)));
    }


    public TarantoolRecord<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPSERT, TarantoolResponseMapping::toValue, space, dataTuple(defaultValue), updateOperationsTuple(operations)));
    }

    public TarantoolRecord<Value> upsert(TarantoolTransactionDependency defaultValueDependency, TarantoolUpdateFieldOperation... operations){
        return cast(transactionManager.callRW(UPSERT, TarantoolResponseMapping::toValue, space, defaultValueDependency.get(), updateOperationsTuple(operations)));
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
        private Map<String, Object> options = MapFactory.map();
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

        public TarantoolRecord<List<Value>> execute(){
            return cast(transactionManager.callRO(SELECT, TarantoolResponseMapping::toValuesList, space, request, index, options));
        }

        public List<Value> get(){
            return execute().get();
        }

        public Stream<Value> stream() {
            return execute().get().stream();
        }
    }
    
}
