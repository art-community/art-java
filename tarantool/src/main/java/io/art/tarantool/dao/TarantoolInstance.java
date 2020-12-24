package io.art.tarantool.dao;

import io.art.tarantool.configuration.space.TarantoolSpaceConfig;
import io.art.tarantool.configuration.space.TarantoolSpaceFormat;
import io.art.tarantool.configuration.space.TarantoolSpaceIndex;
import io.art.tarantool.dao.transaction.TarantoolTransactionManager;
import io.art.tarantool.dao.transaction.result.TarantoolOperationResult;
import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.model.TarantoolResponseMapping;
import io.art.tarantool.module.client.TarantoolClusterClient;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.logging.LoggingModule.logger;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_GET_RESPONSE;
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

    public Set<String> listSpaces(){
        return cast(synchronize(transactionManager().callRO(LIST_SPACES, spaces -> {
            spaces = cast(spaces.get(0));
            return cast(setOf(spaces));
        })));
    }

    public TarantoolSpace space(String space){
        return new TarantoolSpace(this, space);
    }

    public TarantoolAsynchronousSpace asynchronousSpace(String space){
        return new TarantoolAsynchronousSpace(this, space);
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        synchronize(transactionManager().callRW(CREATE_SPACE, TarantoolResponseMapping::noMapping, space, config.getConfig()));
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        synchronize(transactionManager().callRW(FORMAT_SPACE, TarantoolResponseMapping::noMapping, space, format.getFormat()));
    }

    public void createIndex(String space, String indexName, TarantoolSpaceIndex indexConfig){
        synchronize(transactionManager().callRW(CREATE_INDEX, TarantoolResponseMapping::noMapping, space, indexName, indexConfig.getIndex()));
    }

    public void dropIndex(String space, String indexName){
        synchronize(transactionManager().callRW(DROP_INDEX, TarantoolResponseMapping::noMapping, space, indexName));
    }

    public void renameSpace(String space, String newName){
        synchronize(transactionManager().callRW(RENAME_SPACE, TarantoolResponseMapping::noMapping, space, newName));
    }

    public void dropSpace(String space){
        synchronize(transactionManager().callRW(DROP_SPACE, TarantoolResponseMapping::noMapping, space));
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

    protected TarantoolTransactionManager transactionManager(){
        if (isNull(transactionManagerHolder.get())) transactionManagerHolder.set(new TarantoolTransactionManager(client));
        return transactionManagerHolder.get();
    }

    private Object synchronize(TarantoolOperationResult<?> result){
        try {
            return result.get();
        }catch(Throwable throwable){
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, throwable);
        }
    }
}
