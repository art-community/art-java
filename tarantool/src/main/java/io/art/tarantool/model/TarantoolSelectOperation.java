package io.art.tarantool.model;

import io.art.tarantool.transaction.operation.dependency.TarantoolTransactionDependency;

import java.util.Map;

import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.core.factory.MapFactory.map;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.LIMIT;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.OFFSET;

public class TarantoolSelectOperation {
    private Object request;
    private Map<String, Object> options = map();
    private String index;

    public TarantoolSelectOperation(String request){
        this.request = linkedListOf(request);
    }

    public TarantoolSelectOperation(TarantoolTransactionDependency requestDependency){
        this.request = requestDependency.get();
    }

    public TarantoolSelectOperation index(String index){
        this.index = index;
        return this;
    }

    public TarantoolSelectOperation limit(Long limit){
        options.put(LIMIT, limit);
        return this;
    }

    public TarantoolSelectOperation offset(Long offset){
        options.put(OFFSET, offset);
        return this;
    }
}
