package io.art.tarantool.dao;

import io.art.tarantool.dao.transaction.result.TarantoolOperationResult;
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

    public Optional<Value> get(Value key){
        return cast(synchronize(asynchronousSpace.get(key)));
    }

    public Optional<Value> get(String index, Value key){
        return cast(synchronize(asynchronousSpace.get(index, key)));
    }

    public Optional<List<Value>> select(Value request){
        return cast(synchronize(asynchronousSpace.select(request)));
    }

    public Optional<List<Value>> select(String index, Value request){
        return cast(synchronize(asynchronousSpace.select(index, request)));
    }

    public Optional<Value> delete(Value key){
        return cast(synchronize(asynchronousSpace.delete(key)));
    }

    public Optional<Value> insert(Value data){
        return cast(synchronize(asynchronousSpace.insert(data)));
    }

    public Optional<Value> autoIncrement(Value data){
        return cast(synchronize(asynchronousSpace.autoIncrement(data)));
    }

    public Optional<Value> put(Value data){
        return cast(synchronize(asynchronousSpace.put(data)));
    }

    public Optional<Value> replace(Value data){
        return cast(synchronize(asynchronousSpace.replace(data)));
    }

    public Optional<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        return cast(synchronize(asynchronousSpace.update(key, operations)));
    }

    public Optional<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return cast(synchronize(asynchronousSpace.upsert(defaultValue, operations)));
    }

    public Long count(){
        return cast(synchronize(asynchronousSpace.count()));
    }

    public Long len(){
        return cast(synchronize(asynchronousSpace.len()));
    }

    public Long schemaCount(){
        return cast(synchronize(asynchronousSpace.schemaCount()));
    }

    public Long schemaLen(){
        return cast(synchronize(asynchronousSpace.schemaLen()));
    }

    public void truncate(){
        asynchronousSpace.truncate();
    }

    public Set<String> listIndices(){
        return cast(synchronize(asynchronousSpace.listIndices()));
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



    private Object synchronize(TarantoolOperationResult<?> result){
        try {
            return result.get();
        }catch(Throwable throwable){
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, throwable);
        }
    }

}
