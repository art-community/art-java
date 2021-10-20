package io.art.tarantool.instance;

import io.art.logging.logger.*;
import io.art.tarantool.configuration.space.*;
import io.art.tarantool.model.mapping.*;
import io.art.tarantool.model.record.*;
import io.art.tarantool.module.connection.client.*;
import io.art.tarantool.transaction.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

public class TarantoolInstance {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstance.class);

/*
    @Getter
    private final TarantoolTransactionManager transactionManager;


    public TarantoolInstance(Supplier<TarantoolCluster> client) {
        transactionManager = new TarantoolTransactionManager(client);
    }

    public TarantoolRecord<Set<String>> listSpaces() {
        return cast(transactionManager.callRO(LIST_SPACES, TarantoolResponseMapping::toStringSet).synchronize());
    }

*/
/*
    public TarantoolSpace<Value, Value> space(String space) {
        return cast(TarantoolSpaceImplementation.builder()
                .space(space)
                .transactionManager(transactionManager)
                .fromModelMapper(cast(ValueFromModelMapper.identity()))
                .toModelMapper(cast(ValueToModelMapper.identity()))
                .keyMapper(cast(ValueFromModelMapper.identity()))
                .build());
    }

    @Builder(builderMethodName = "spaceBuilder")
    public <T, K> TarantoolSpace<T, K> space(String space,
                                             Function<Value, T> toModelMapper,
                                             Function<T, Value> fromModelMapper,
                                             Function<K, Value> keyMapper) {
        return cast(TarantoolSpaceImplementation.builder()
                .space(space)
                .transactionManager(transactionManager)
                .fromModelMapper(cast(fromModelMapper))
                .toModelMapper(cast(toModelMapper))
                .keyMapper(cast(keyMapper))
                .build());
    }
*//*


    public void createSpace(String space, TarantoolSpaceConfig config) {
        transactionManager.callRW(CREATE_SPACE, TarantoolResponseMapping::toEmpty, space, config.getConfig()).synchronize();
    }

    public void formatSpace(String space, TarantoolSpaceFormat format) {
        transactionManager.callRW(FORMAT_SPACE, TarantoolResponseMapping::toEmpty, space, format.getFormat()).synchronize();
    }

    public void createIndex(String space, String indexName, TarantoolSpaceIndex indexConfig) {
        transactionManager.callRW(CREATE_INDEX, TarantoolResponseMapping::toEmpty, space, indexName, indexConfig.getIndex()).synchronize();
    }

    public void dropIndex(String space, String indexName) {
        transactionManager.callRW(DROP_INDEX, TarantoolResponseMapping::toEmpty, space, indexName).synchronize();
    }

    public void renameSpace(String space, String newName) {
        transactionManager.callRW(RENAME_SPACE, TarantoolResponseMapping::toEmpty, space, newName).synchronize();
    }

    public void dropSpace(String space) {
        transactionManager.callRW(DROP_SPACE, TarantoolResponseMapping::toEmpty, space).synchronize();
    }

    public void beginTransaction() {
        transactionManager.begin();
    }

    public void beginTransaction(Long bucketId) {
        transactionManager.begin(bucketId);
    }

    public void commitTransaction() {
        transactionManager.commit();
    }

    public void cancelTransaction() {
        transactionManager.cancel();
    }
*/
}
