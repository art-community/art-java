package io.art.tarantool.dao;

import io.art.tarantool.transaction.operation.dependency.TarantoolTransactionDependency;
import io.art.tarantool.transaction.operation.result.TarantoolOperationResult;
import io.art.tarantool.model.TarantoolUpdateFieldOperation;
import io.art.value.immutable.Value;
import io.art.tarantool.model.TarantoolResponseMapping;
import lombok.AllArgsConstructor;
import lombok.NonNull;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.model.TarantoolRequest.*;

@AllArgsConstructor
public class TarantoolAsynchronousSpace {
    @NonNull
    private final TarantoolInstance tarantoolInstance;
    @NonNull
    private final String space;


    public TarantoolOperationResult<Value> get(Value key){
        return cast(callRO(GET, TarantoolResponseMapping::toValue, space, requestTuple(key)));
    }

    public TarantoolOperationResult<Value> get(TarantoolTransactionDependency keyDependency){
        return cast(callRO(GET, TarantoolResponseMapping::toValue, space, keyDependency.get()));
    }

    public TarantoolOperationResult<Value> get(String index, Value key){
        return cast(callRO(GET, TarantoolResponseMapping::toValue, space, index, requestTuple(key)));
    }

    public TarantoolOperationResult<Value> get(String index, TarantoolTransactionDependency keyDependency){
        return cast(callRO(GET, TarantoolResponseMapping::toValue, space, index, keyDependency.get()));
    }


    public TarantoolOperationResult<List<Value>> select(Value request){
        return cast(callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestTuple(request)));
    }

    public TarantoolOperationResult<List<Value>> select(String index, Value request){
        return cast(callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestTuple(request), index));
    }

    public TarantoolOperationResult<List<Value>> select(TarantoolTransactionDependency requestDependency){
        return cast(callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestDependency.get()));
    }

    public TarantoolOperationResult<List<Value>> select(String index, TarantoolTransactionDependency requestDependency){
        return cast(callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestDependency.get(), index));
    }


    public TarantoolOperationResult<Value> delete(Value key){
        return cast(callRW(DELETE, TarantoolResponseMapping::toValue, space, requestTuple(key)));
    }

    public TarantoolOperationResult<Value> delete(TarantoolTransactionDependency keyDependency){
        return cast(callRW(DELETE, TarantoolResponseMapping::toValue, space, keyDependency.get()));
    }


    public TarantoolOperationResult<Value> insert(Value data){
        return cast(callRW(INSERT, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolOperationResult<Value> insert(TarantoolTransactionDependency dataDependency){
        return cast(callRW(INSERT, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolOperationResult<Value> autoIncrement(Value data){
        return cast(callRW(AUTO_INCREMENT, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolOperationResult<Value> autoIncrement(TarantoolTransactionDependency dataDependency){
        return cast(callRW(AUTO_INCREMENT, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolOperationResult<Value> put(Value data){
        return cast(callRW(PUT, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolOperationResult<Value> put(TarantoolTransactionDependency dataDependency){
        return cast(callRW(PUT, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolOperationResult<Value> replace(Value data){
        return cast(callRW(REPLACE, TarantoolResponseMapping::toValue, space, dataTuple(data)));
    }

    public TarantoolOperationResult<Value> replace(TarantoolTransactionDependency dataDependency){
        return cast(callRW(REPLACE, TarantoolResponseMapping::toValue, space, dataDependency.get()));
    }


    public TarantoolOperationResult<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        return cast(callRW(UPDATE, TarantoolResponseMapping::toValue, space, requestTuple(key), updateOperationsTuple(operations)));
    }

    public TarantoolOperationResult<Value> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return cast(callRW(UPDATE, TarantoolResponseMapping::toValue, space, keyDependency.get(), updateOperationsTuple(operations)));
    }


    public TarantoolOperationResult<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return cast(callRW(UPSERT, TarantoolResponseMapping::toValue, space, dataTuple(defaultValue), updateOperationsTuple(operations)));
    }

    public TarantoolOperationResult<Value> upsert(TarantoolTransactionDependency defaultValueDependency, TarantoolUpdateFieldOperation... operations){
        return cast(callRW(UPSERT, TarantoolResponseMapping::toValue, space, defaultValueDependency.get(), updateOperationsTuple(operations)));
    }



    public TarantoolOperationResult<Long> count(){
        return cast(callRO(COUNT, TarantoolResponseMapping::toLong, space));
    }

    public TarantoolOperationResult<Long> len(){
        return cast(callRO(LEN, TarantoolResponseMapping::toLong, space));
    }

    public TarantoolOperationResult<Long> schemaCount(){
        return cast(callRO(SCHEMA_COUNT, TarantoolResponseMapping::toLong, space));
    }

    public TarantoolOperationResult<Long> schemaLen(){
        return cast(callRO(SCHEMA_LEN, TarantoolResponseMapping::toLong, space));
    }

    public void truncate(){
        callRW(TRUNCATE, TarantoolResponseMapping::toEmpty, space);
    }

    public TarantoolOperationResult<Set<String>> listIndices(){
        return cast(callRO(LIST_INDICES, TarantoolResponseMapping::toStringSet, space));
    }

    public void beginTransaction(){
        tarantoolInstance.transactionManager().begin();
    }

    public void commitTransaction(){
        tarantoolInstance.transactionManager().commit();
    }

    public void cancelTransaction(){
        tarantoolInstance.transactionManager().cancel();
    }



    private TarantoolOperationResult<?> callRO(String function, Function<List<?>, Optional<?>> mapper, Object ... args){
        return tarantoolInstance.transactionManager().callRO(function, mapper, args);
    }

    private TarantoolOperationResult<?> callRW(String function, Function<List<?>, Optional<?>> mapper, Object ... args){
        return tarantoolInstance.transactionManager().callRW(function, mapper, args);
    }
}
