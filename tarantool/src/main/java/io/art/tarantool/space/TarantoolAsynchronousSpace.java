package io.art.tarantool.space;

import io.art.tarantool.transaction.TarantoolTransactionManager;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.model.record.TarantoolRecord;
import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation;
import io.art.value.immutable.Value;
import io.art.tarantool.model.mapping.TarantoolResponseMapping;
import lombok.AllArgsConstructor;
import lombok.NonNull;


import java.util.List;
import java.util.Set;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.model.mapping.TarantoolRequestMapping.*;

@AllArgsConstructor
public class TarantoolAsynchronousSpace {
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


    public TarantoolRecord<List<Value>> select(Value request){
        return cast(transactionManager.callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestTuple(request)));
    }

    public TarantoolRecord<List<Value>> select(String index, Value request){
        return cast(transactionManager.callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestTuple(request), index));
    }

    public TarantoolRecord<List<Value>> select(TarantoolTransactionDependency requestDependency){
        return cast(transactionManager.callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestDependency.get()));
    }

    public TarantoolRecord<List<Value>> select(String index, TarantoolTransactionDependency requestDependency){
        return cast(transactionManager.callRO(SELECT, TarantoolResponseMapping::toValuesList, space, requestDependency.get(), index));
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

    public TarantoolRecord<Long> schemaCount(){
        return cast(transactionManager.callRO(SCHEMA_COUNT, TarantoolResponseMapping::toLong, space));
    }

    public TarantoolRecord<Long> schemaLen(){
        return cast(transactionManager.callRO(SCHEMA_LEN, TarantoolResponseMapping::toLong, space));
    }

    public void truncate(){
        transactionManager.callRW(TRUNCATE, TarantoolResponseMapping::toEmpty, space);
    }

    public TarantoolRecord<Set<String>> listIndices(){
        return cast(transactionManager.callRO(LIST_INDICES, TarantoolResponseMapping::toStringSet, space));
    }
    
}
