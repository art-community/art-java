package io.art.tarantool.dao.transaction.result;

import java.util.concurrent.ExecutionException;

public interface TarantoolOperationResult <T>{
    public T get() throws ExecutionException, InterruptedException;

    public Object useResult();

    public Object useResultField(String fieldName);

    public boolean isDone();

}
