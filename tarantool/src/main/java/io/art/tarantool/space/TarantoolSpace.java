package io.art.tarantool.space;

import io.art.tarantool.model.operation.TarantoolUpdateFieldOperation;
import io.art.tarantool.transaction.TarantoolTransactionManager;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.model.record.TarantoolRecord;
import io.art.value.immutable.Value;
import lombok.*;
import org.apache.logging.log4j.*;

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


    public TarantoolRecord<Value> get(Value key){
        return asynchronousSpace.get(key).synchronize();
    }

    public TarantoolRecord<Value> get(TarantoolTransactionDependency keyDependency){
        return asynchronousSpace.get(keyDependency).synchronize();
    }

    public TarantoolRecord<Value> get(String index, Value key){
        return asynchronousSpace.get(index, key).synchronize();
    }

    public TarantoolRecord<Value> get(String index, TarantoolTransactionDependency keyDependency){
        return asynchronousSpace.get(index, keyDependency).synchronize();
    }


    public TarantoolRecord<List<Value>> select(Value request){
        return asynchronousSpace.select(request).synchronize();
    }

    public TarantoolRecord<List<Value>> select(TarantoolTransactionDependency requestDependency){
        return asynchronousSpace.select(requestDependency).synchronize();
    }

    public TarantoolRecord<List<Value>> select(String index, Value request){
        return asynchronousSpace.select(index, request).synchronize();
    }

    public TarantoolRecord<List<Value>> select(String index, TarantoolTransactionDependency requestDependency){
        return asynchronousSpace.select(index, requestDependency).synchronize();
    }


    public TarantoolRecord<Value> delete(Value key){
        return asynchronousSpace.delete(key).synchronize();
    }

    public TarantoolRecord<Value> delete(TarantoolTransactionDependency keyDependency){
        return asynchronousSpace.delete(keyDependency).synchronize();
    }


    public TarantoolRecord<Value> insert(Value data){
        return asynchronousSpace.insert(data).synchronize();
    }

    public TarantoolRecord<Value> insert(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.insert(dataDependency).synchronize();
    }


    public TarantoolRecord<Value> autoIncrement(Value data){
        return asynchronousSpace.autoIncrement(data).synchronize();
    }

    public TarantoolRecord<Value> autoIncrement(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.autoIncrement(dataDependency).synchronize();
    }


    public TarantoolRecord<Value> put(Value data){
        return asynchronousSpace.put(data).synchronize();
    }

    public TarantoolRecord<Value> put(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.put(dataDependency).synchronize();
    }


    public TarantoolRecord<Value> replace(Value data){
        return asynchronousSpace.replace(data).synchronize();
    }

    public TarantoolRecord<Value> replace(TarantoolTransactionDependency dataDependency){
        return asynchronousSpace.replace(dataDependency).synchronize();
    }


    public TarantoolRecord<Value> update(Value key, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.update(key, operations).synchronize();
    }

    public TarantoolRecord<Value> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.update(keyDependency, operations).synchronize();
    }


    public TarantoolRecord<Value> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.upsert(defaultValue, operations).synchronize();
    }

    public TarantoolRecord<Value> upsert(TarantoolTransactionDependency defaultValueDependency, TarantoolUpdateFieldOperation... operations){
        return asynchronousSpace.upsert(defaultValueDependency, operations).synchronize();
    }


    public TarantoolRecord<Long> count(){
        return asynchronousSpace.count().synchronize();
    }

    public TarantoolRecord<Long> len(){
        return asynchronousSpace.len().synchronize();
    }

    public TarantoolRecord<Long> schemaCount(){
        return asynchronousSpace.schemaCount().synchronize();
    }

    public TarantoolRecord<Long> schemaLen(){
        return asynchronousSpace.schemaLen().synchronize();
    }

    public void truncate(){
        asynchronousSpace.truncate();
    }

    public TarantoolRecord<Set<String>> listIndices(){
        return asynchronousSpace.listIndices().synchronize();
    }

}
