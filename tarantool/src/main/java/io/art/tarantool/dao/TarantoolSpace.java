package io.art.tarantool.dao;

import io.art.tarantool.transaction.TarantoolTransactionManager;
import io.art.tarantool.transaction.operation.dependency.TarantoolTransactionDependency;
import io.art.tarantool.transaction.operation.result.TarantoolOperationResult;
import io.art.value.immutable.Value;
import lombok.*;
import org.apache.logging.log4j.*;
import io.art.tarantool.model.*;

import java.util.*;

import static io.art.logging.LoggingModule.*;
import static lombok.AccessLevel.*;

public class TarantoolSpace {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolSpace.class);
    private final TarantoolAsynchronousSpace asynchronousSpace;

    public TarantoolSpace(TarantoolTransactionManager transactionManager, String space){
        this.asynchronousSpace = new TarantoolAsynchronousSpace(transactionManager, space);
    }


    public TarantoolOperationResult<Value> get(Value key){
        return asynchronousSpace.get(key).synchronize();
    }

    public TarantoolOperationResult<Value> get(TarantoolTransactionDependency keyDependency){
        return asynchronousSpace.get(keyDependency).synchronize();
    }

    public TarantoolOperationResult<Value> get(String index, Value key){
        return asynchronousSpace.get(index, key).synchronize();
    }

    public TarantoolOperationResult<Value> get(String index, TarantoolTransactionDependency keyDependency){
        return asynchronousSpace.get(index, keyDependency).synchronize();
    }


    public TarantoolOperationResult<List<Value>> select(Value request){
        return asynchronousSpace.select(request).synchronize();
    }

    public TarantoolOperationResult<List<Value>> select(TarantoolTransactionDependency requestDependency){
        return asynchronousSpace.select(requestDependency).synchronize();
    }

    public TarantoolOperationResult<List<Value>> select(String index, Value request){
        return asynchronousSpace.select(index, request).synchronize();
    }

    public TarantoolOperationResult<List<Value>> select(String index, TarantoolTransactionDependency requestDependency){
        return asynchronousSpace.select(index, requestDependency).synchronize();
    }


    public TarantoolOperationResult<Value> delete(Value key){
        return asynchronousSpace.delete(key).synchronize();
    }

    public TarantoolOperationResult<Value> delete(TarantoolTransactionDependency keyDependency){
        return asynchronousSpace.delete(keyDependency).synchronize();
    }


    public TarantoolOperationResult<Value> insert(Value data){
        return asynchronousSpace.insert(data).synchronize();
    }

    public TarantoolOperationResult<Value> insert(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.insert(dataDependency).synchronize();
    }


    public TarantoolOperationResult<Value> autoIncrement(Value data){
        return asynchronousSpace.autoIncrement(data).synchronize();
    }

    public TarantoolOperationResult<Value> autoIncrement(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.autoIncrement(dataDependency).synchronize();
    }


    public TarantoolOperationResult<Value> put(Value data){
        return asynchronousSpace.put(data).synchronize();
    }

    public TarantoolOperationResult<Value> put(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.put(dataDependency).synchronize();
    }


    public TarantoolOperationResult<Value> replace(Value data){
        return asynchronousSpace.replace(data).synchronize();
    }

    public TarantoolOperationResult<Value> replace(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.replace(dataDependency).synchronize();
    }


    public TarantoolOperationResult<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.update(key, operations).synchronize();
    }

    public TarantoolOperationResult<Value> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.update(keyDependency, operations).synchronize();
    }


    public TarantoolOperationResult<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.upsert(defaultValue, operations).synchronize();
    }

    public TarantoolOperationResult<Value> upsert(TarantoolTransactionDependency defaultValueDependency, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.upsert(defaultValueDependency, operations).synchronize();
    }


    public TarantoolOperationResult<Long> count(){
        return asynchronousSpace.count().synchronize();
    }

    public TarantoolOperationResult<Long> len(){
        return asynchronousSpace.len().synchronize();
    }

    public TarantoolOperationResult<Long> schemaCount(){
        return asynchronousSpace.schemaCount().synchronize();
    }

    public TarantoolOperationResult<Long> schemaLen(){
        return asynchronousSpace.schemaLen().synchronize();
    }

    public void truncate(){
        asynchronousSpace.truncate();
    }

    public TarantoolOperationResult<Set<String>> listIndices(){
        return asynchronousSpace.listIndices().synchronize();
    }

}
