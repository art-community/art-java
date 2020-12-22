package io.art.tarantool.dao;

import io.art.tarantool.configuration.space.TarantoolSpaceConfig;
import io.art.tarantool.configuration.space.TarantoolSpaceFormat;
import io.art.tarantool.configuration.space.TarantoolSpaceIndex;
import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.module.client.TarantoolClusterClient;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.logging.LoggingModule.logger;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.UNABLE_TO_GET_RESPONSE;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static lombok.AccessLevel.PRIVATE;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstance.class);
    private final TarantoolClusterClient client;

    public TarantoolInstance(TarantoolClusterClient client){
        this.client = client;
    }

    public Set<String> listSpaces(){
        List<?> response = cast(synchronize(client.callRO(LIST_SPACES)));
        response = cast(response.get(0));
        return cast(setOf(response));
    }

    public TarantoolSpace space(String space){
        return new TarantoolSpace(client, space);
    }

    public TarantoolAsynchronousSpace asynchronousSpace(String space){
        return new TarantoolAsynchronousSpace(client, space);
    }

    public void createSpace(String space, TarantoolSpaceConfig config){
        synchronize(client.callRW(CREATE_SPACE, space, config.getConfig()));
    }

    public void formatSpace(String space, TarantoolSpaceFormat format){
        synchronize(client.callRW(FORMAT_SPACE, space, format.getFormat()));
    }

    public void createIndex(String space, String indexName, TarantoolSpaceIndex indexConfig){
        synchronize(client.callRW(CREATE_INDEX, space, indexName, indexConfig.getIndex()));
    }

    public void dropIndex(String space, String indexName){
        synchronize(client.callRW(DROP_INDEX, space, indexName));
    }

    public void renameSpace(String space, String newName){
        synchronize(client.callRW(RENAME_SPACE, space, newName));
    }

    public void dropSpace(String space){
        synchronize(client.callRW(DROP_SPACE, space));
    }

    private Object synchronize(CompletableFuture<?> future){
        try {
            return future.get();
        }catch(Throwable throwable){
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, throwable);
        }
    }
}
