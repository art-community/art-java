package ru.art.tarantool.dao;

import org.tarantool.TarantoolClient;
import ru.art.entity.Entity;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.*;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.arrayOf;
import static ru.art.entity.Value.asEntity;
import static ru.art.entity.tuple.PlainTupleReader.readTuple;
import static ru.art.entity.tuple.schema.ValueSchema.fromTuple;
import static ru.art.tarantool.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModuleState;
import static ru.art.tarantool.service.TarantoolScriptService.evaluateValueScript;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("Duplicates")
public final class TarantoolIndexDao extends TarantoolCommonDao {
    TarantoolIndexDao(String instanceId) {
        super(instanceId);
    }

    public Optional<Entity> getByIndex(String spaceName, String indexName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = callTarantoolFunction(client, GET + VALUE_POSTFIX + spaceName + BY + indexName, keys);
        List<?> tuple = (List<?>) result.get(0);
        if (isEmpty(result) || isEmpty(tuple)) {
            return empty();
        }
        List<?> schema = (List<?>) result.get(1);
        if (isEmpty(schema)) {
            return empty();
        }
        return of(asEntity(readTuple(tuple, fromTuple(schema))));
    }

    public Optional<Entity> getByIndex(String spaceName, String indexName) {
        return getByIndex(spaceName, indexName, emptySet());
    }


    public List<Entity> selectByIndex(String spaceName, String indexName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName, indexName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<List<?>> result = cast(callTarantoolFunction(client, SELECT + VALUES_POSTFIX + spaceName + BY + indexName, keys));
        List<List<?>> tuples = cast(result.get(0));
        if (isEmpty(result) || isEmpty(tuples)) {
            return emptyList();
        }
        List<List<?>> schemes = cast(result.get(1));
        List<Entity> values = arrayOf(tuples.size());
        for (int i = 0; i < tuples.size(); i++) {
            values.add(asEntity(readTuple(tuples.get(i), fromTuple(schemes.get(i)))));
        }
        return values;
    }

    public List<Entity> selectAllByIndex(String spaceName, String indexName) {
        return selectByIndex(spaceName, indexName, emptySet());
    }


    public Entity deleteByIndex(String spaceName, String indexName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName, indexName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<List<?>> result = cast(callTarantoolFunction(client, DELETE + VALUES_POSTFIX + spaceName + BY + indexName, keys));
        List<List<?>> tuple = cast(result.get(0));
        if (isEmpty(result) || isEmpty(tuple)) {
            return null;
        }
        List<List<?>> schema = cast(result.get(1));
        return asEntity(readTuple(tuple, fromTuple(schema)));
    }


    public long countByIndex(String spaceName, String indexName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName, indexName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = callTarantoolFunction(client, COUNT + VALUES_POSTFIX + spaceName + BY + indexName, keys);
        if (isEmpty(result)) {
            return 0L;
        }
        return ((Number) ((List<?>) result.get(0)).get(0)).longValue();
    }

    public long countByIndex(String spaceName, String indexName) {
        return countByIndex(spaceName, indexName, emptySet());
    }

    public long lenByIndex(String spaceName, String indexName) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = callTarantoolFunction(client, LEN + VALUES_POSTFIX + spaceName + BY + indexName);
        if (isEmpty(result)) {
            return 0L;
        }
        return ((Number) ((List<?>) result.get(0)).get(0)).longValue();
    }
}
