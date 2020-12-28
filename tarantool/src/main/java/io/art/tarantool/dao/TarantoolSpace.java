package io.art.tarantool.dao;

import io.art.tarantool.transaction.operation.result.TarantoolOperationResult;
import io.art.value.immutable.Value;
import lombok.*;
import org.apache.logging.log4j.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.*;
import static io.art.core.caster.Caster.cast;
import java.util.*;

import static io.art.logging.LoggingModule.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_GET_RESPONSE;
import static lombok.AccessLevel.*;

public class TarantoolSpace {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolSpace.class);
    private final TarantoolAsynchronousSpace asynchronousSpace;

    public TarantoolSpace(TarantoolInstance instance, String space){
        this.asynchronousSpace = new TarantoolAsynchronousSpace(instance, space);
    }

    public TarantoolOperationResult<Value> get(Value key){
        return asynchronousSpace.get(key).synchronize();
    }

    public TarantoolOperationResult<Value> get(String index, Value key){
        return asynchronousSpace.get(index, key).synchronize();
    }

    public TarantoolOperationResult<List<Value>> select(Value request){
        return asynchronousSpace.select(request).synchronize();
    }

    public TarantoolOperationResult<List<Value>> select(String index, Value request){
        return asynchronousSpace.select(index, request).synchronize();
    }

    public TarantoolOperationResult<Value> delete(Value key){
        return asynchronousSpace.delete(key).synchronize();
    }

    public TarantoolOperationResult<Value> insert(Value data){
        return asynchronousSpace.insert(data).synchronize();
    }

    public TarantoolOperationResult<Value> autoIncrement(Value data){
        return asynchronousSpace.autoIncrement(data).synchronize();
    }

    public TarantoolOperationResult<Value> put(Value data){
        return asynchronousSpace.put(data).synchronize();
    }

    public TarantoolOperationResult<Value> replace(Value data){
        return asynchronousSpace.replace(data).synchronize();
    }

    public TarantoolOperationResult<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.update(key, operations).synchronize();
    }

    public TarantoolOperationResult<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.upsert(defaultValue, operations).synchronize();
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

    public void beginTransaction(){
        asynchronousSpace.beginTransaction();
    }

    public void commitTransaction(){
        asynchronousSpace.commitTransaction();
    }

    public void cancelTransaction(){
        asynchronousSpace.cancelTransaction();
    }





}
