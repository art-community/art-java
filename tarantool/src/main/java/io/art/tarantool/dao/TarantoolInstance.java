package io.art.tarantool.dao;

import io.art.tarantool.configuration.space.TarantoolSpaceConfig;
import io.art.tarantool.configuration.space.TarantoolSpaceFormat;
import io.art.tarantool.configuration.space.TarantoolSpaceIndex;
import io.art.tarantool.transaction.TarantoolTransactionManager;
import io.art.tarantool.transaction.operation.result.TarantoolOperationResult;
import io.art.tarantool.model.TarantoolResponseMapping;
import io.art.tarantool.module.client.TarantoolClusterClient;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.Set;

import static io.art.core.caster.Caster.cast;
import static io.art.logging.LoggingModule.logger;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstance.class);
    private final ThreadLocal<TarantoolTransactionManager> transactionManagerHolder = new ThreadLocal<>();
    protected final TarantoolClusterClient client;


    public TarantoolInstance(TarantoolClusterClient client){
        this.client = client;
    }

    public TarantoolOperationResult<Set<String>> listSpaces(){
        return cast(transactionManager().callRO(LIST_SPACES, TarantoolResponseMapping::toStringSet).synchronize());
    }

    public TarantoolSpace space(String space){
        return new TarantoolSpace(transactionManager(), space);
    }

    public TarantoolAsynchronousSpace asynchronousSpace(String space){
        return new TarantoolAsynchronousSpace(transactionManager(), space);
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        transactionManager().callRW(CREATE_SPACE, TarantoolResponseMapping::toEmpty, space, config.getConfig()).synchronize();
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        transactionManager().callRW(FORMAT_SPACE, TarantoolResponseMapping::toEmpty, space, format.getFormat()).synchronize();
    }

    public void createIndex(String space, String indexName, TarantoolSpaceIndex indexConfig){
        transactionManager().callRW(CREATE_INDEX, TarantoolResponseMapping::toEmpty, space, indexName, indexConfig.getIndex()).synchronize();
    }

    public void dropIndex(String space, String indexName){
        transactionManager().callRW(DROP_INDEX, TarantoolResponseMapping::toEmpty, space, indexName).synchronize();
    }

    public void renameSpace(String space, String newName){
        transactionManager().callRW(RENAME_SPACE, TarantoolResponseMapping::toEmpty, space, newName).synchronize();
    }

    public void dropSpace(String space){
        transactionManager().callRW(DROP_SPACE, TarantoolResponseMapping::toEmpty, space).synchronize();
    }

    public void beginTransaction(){
        transactionManager().begin();
    }

    public void commitTransaction(){
        transactionManager().commit();
    }

    public void cancelTransaction(){
        transactionManager().cancel();
    }

    private TarantoolTransactionManager transactionManager(){
        if (isNull(transactionManagerHolder.get())) transactionManagerHolder.set(new TarantoolTransactionManager(client));
        return transactionManagerHolder.get();
    }

}
