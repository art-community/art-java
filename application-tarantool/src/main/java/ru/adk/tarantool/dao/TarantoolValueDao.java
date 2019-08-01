
package ru.adk.tarantool.dao;

import org.tarantool.TarantoolClient;
import ru.adk.entity.*;
import ru.adk.tarantool.exception.TarantoolDaoException;
import ru.adk.tarantool.model.TarantoolUpdateFieldOperation;
import static java.text.MessageFormat.format;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.factory.CollectionsFactory.*;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.PrimitivesFactory.longPrimitive;
import static ru.adk.entity.Value.*;
import static ru.adk.entity.tuple.PlainTupleReader.readTuple;
import static ru.adk.entity.tuple.PlainTupleWriter.PlainTupleWriterResult;
import static ru.adk.entity.tuple.PlainTupleWriter.writeTuple;
import static ru.adk.entity.tuple.schema.ValueSchema.fromTuple;
import static ru.adk.tarantool.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.ENTITY_IS_NULL;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.ENTITY_WITHOUT_ID_FILED;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.ID;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolIdCalculationMode.SEQUENCE;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.VALUE;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModuleState;
import static ru.adk.tarantool.service.TarantoolScriptService.evaluateValueScript;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("Duplicates")
public final class TarantoolValueDao extends TarantoolCommonDao {
    TarantoolValueDao(String instanceId) {
        super(instanceId);
    }

    public Entity put(String spaceName, Entity entity) {
        if (isNull(entity)) {
            throw new TarantoolDaoException(format(ENTITY_IS_NULL, spaceName));
        }
        if (!entity.getFieldNames().contains(ID)) {
            throw new TarantoolDaoException(format(ENTITY_WITHOUT_ID_FILED, spaceName));
        }
        if (idCalculationMode == SEQUENCE) {
            entity.getFields().put(ID, cast(longPrimitive(null)));
        }
        evaluateValueScript(instanceId, spaceName);
        PlainTupleWriterResult valueTupleResult = writeTuple(entity);
        if (isNull(valueTupleResult)) {
            throw new TarantoolDaoException(format(ENTITY_IS_NULL, spaceName));
        }
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        String valueFunctionName = PUT + spaceName + VALUE_POSTFIX;
        List<?> valueTuple = valueTupleResult.getTuple();
        List<?> schemaTuple = valueTupleResult.getSchema().toTuple();
        List<List<?>> result = cast(callTarantoolFunction(client, valueFunctionName, fixedArrayOf(valueTuple, schemaTuple)));
        return asEntity(readTuple(result.get(0), fromTuple(result.get(1))));
    }

    public Entity put(String spaceName, Long id, Primitive primitive) {
        return put(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, primitive).build());
    }

    public Entity put(String spaceName, Long id, CollectionValue<?> collectionValue) {
        return put(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, collectionValue).build());
    }

    public Entity put(String spaceName, Long id, StringParametersMap stringParameters) {
        return put(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, stringParameters).build());
    }

    public Entity put(String spaceName, Long id, MapValue mapValue) {
        return put(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, mapValue).build());
    }


    public Entity put(String spaceName, Primitive primitive) {
        return put(spaceName, 1L, primitive);
    }

    public Entity put(String spaceName, CollectionValue<?> collectionValue) {
        return put(spaceName, 1L, collectionValue);
    }

    public Entity put(String spaceName, StringParametersMap stringParameters) {
        return put(spaceName, 1L, stringParameters);
    }

    public Entity put(String spaceName, MapValue mapValue) {
        return put(spaceName, 1L, mapValue);
    }


    public Optional<Entity> get(String spaceName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = callTarantoolFunction(client, GET + spaceName + VALUE_POSTFIX, keys);
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

    public Optional<Entity> get(String spaceName, long id) {
        return get(spaceName, setOf(id));
    }


    public Optional<Primitive> getPrimitive(String spaceName, long id) {
        return get(spaceName, id).map(value -> asPrimitive(asEntity(value).getValue(VALUE)));
    }

    public Optional<Primitive> getPrimitive(String spaceName) {
        return getPrimitive(spaceName, 1);
    }


    public Optional<CollectionValue<?>> getCollectionValue(String spaceName, long id) {
        return get(spaceName, id).map(value -> asCollection(asEntity(value).getValue(VALUE)));
    }

    public Optional<CollectionValue<?>> getCollectionValue(String spaceName) {
        return getCollectionValue(spaceName, 1);
    }


    public Optional<StringParametersMap> getStringParameters(String spaceName, long id) {
        return get(spaceName, id).map(value -> asStringParametersMap(asEntity(value).getValue(VALUE)));
    }

    public Optional<StringParametersMap> getStringParameters(String spaceName) {
        return getStringParameters(spaceName, 1);
    }


    public Optional<MapValue> getMapValue(String spaceName, long id) {
        return get(spaceName, id).map(value -> asMap(asEntity(value).getValue(VALUE)));
    }

    public Optional<MapValue> getMapValue(String spaceName) {
        return getMapValue(spaceName, 1);
    }


    public List<Entity> select(String spaceName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<List<?>> result = cast(callTarantoolFunction(client, SELECT + spaceName + VALUES_POSTFIX, keys));
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

    public List<Entity> select(String spaceName, long id) {
        return select(spaceName, setOf(id));
    }

    public List<Entity> selectAll(String spaceName) {
        return select(spaceName, emptySet());
    }


    public List<Primitive> selectPrimitives(String spaceName, Set<?> keys) {
        return select(spaceName, keys).stream().map(entity -> asPrimitive(entity.getValue(VALUE))).collect(toList());
    }

    public List<Primitive> selectPrimitives(String spaceName, long id) {
        return selectPrimitives(spaceName, setOf(id));
    }

    public List<Primitive> selectAllPrimitives(String spaceName) {
        return selectPrimitives(spaceName, emptySet());
    }


    public List<CollectionValue<?>> selectCollections(String spaceName, Set<?> keys) {
        return select(spaceName, keys).stream().map(entity -> asCollection(entity.getValue(VALUE))).collect(toList());
    }

    public List<CollectionValue<?>> selectCollections(String spaceName, long id) {
        return selectCollections(spaceName, setOf(id));
    }

    public List<CollectionValue<?>> selectAllCollections(String spaceName) {
        return selectCollections(spaceName, emptySet());
    }


    public List<StringParametersMap> selectStringParameters(String spaceName, Set<?> keys) {
        return select(spaceName, keys).stream().map(entity -> asStringParametersMap(entity.getValue(VALUE))).collect(toList());
    }

    public List<StringParametersMap> selectStringParameters(String spaceName, long id) {
        return selectStringParameters(spaceName, setOf(id));
    }

    public List<StringParametersMap> selectAllStringParameters(String spaceName) {
        return selectStringParameters(spaceName, emptySet());
    }


    public List<MapValue> selectMaps(String spaceName, Set<?> keys) {
        return select(spaceName, keys).stream().map(entity -> asMap(entity.getValue(VALUE))).collect(toList());
    }

    public List<MapValue> selectMaps(String spaceName, long id) {
        return selectMaps(spaceName, setOf(id));
    }

    public List<MapValue> selectAllMaps(String spaceName) {
        return selectMaps(spaceName, emptySet());
    }


    public Entity insert(String spaceName, Entity entity) {
        if (isNull(entity)) {
            throw new TarantoolDaoException(format(ENTITY_IS_NULL, spaceName));
        }
        if (!entity.getFieldNames().contains(ID)) {
            throw new TarantoolDaoException(format(ENTITY_WITHOUT_ID_FILED, spaceName));
        }
        if (idCalculationMode == SEQUENCE) {
            entity.getFields().put(ID, cast(longPrimitive(null)));
        }
        evaluateValueScript(instanceId, spaceName);
        PlainTupleWriterResult valueTupleResult = writeTuple(entity);
        if (isNull(valueTupleResult)) {
            throw new TarantoolDaoException(format(ENTITY_IS_NULL, spaceName));
        }
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        String valueFunctionName = INSERT + spaceName + VALUE_POSTFIX;
        List<?> valueTuple = valueTupleResult.getTuple();
        List<?> schemaTuple = valueTupleResult.getSchema().toTuple();
        List<List<?>> result = cast(callTarantoolFunction(client, valueFunctionName, fixedArrayOf(valueTuple, schemaTuple)));
        return asEntity(readTuple(result.get(0), fromTuple(result.get(1))));
    }

    public Entity insert(String spaceName, Long id, Primitive primitive) {
        return insert(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, primitive).build());
    }

    public Entity insert(String spaceName, Long id, CollectionValue<?> collectionValue) {
        return insert(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, collectionValue).build());
    }

    public Entity insert(String spaceName, Long id, StringParametersMap stringParameters) {
        return insert(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, stringParameters).build());
    }

    public Entity insert(String spaceName, Long id, MapValue mapValue) {
        return insert(spaceName, entityBuilder().longField(ID, id).valueField(VALUE, mapValue).build());
    }


    public Entity insert(String spaceName, Primitive primitive) {
        return insert(spaceName, 1L, primitive);
    }

    public Entity insert(String spaceName, CollectionValue<?> collectionValue) {
        return insert(spaceName, 1L, collectionValue);
    }

    public Entity insert(String spaceName, StringParametersMap stringParameters) {
        return insert(spaceName, 1L, stringParameters);
    }

    public Entity insert(String spaceName, MapValue mapValue) {
        return insert(spaceName, 1L, mapValue);
    }


    public Optional<Entity> delete(String spaceName, Set<?> keys) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<List<?>> result = cast(callTarantoolFunction(client, DELETE + spaceName + VALUES_POSTFIX, keys));
        if (isEmpty(result)) {
            return empty();
        }
        List<List<?>> tuple = cast(result.get(0));
        if (isEmpty(tuple)) {
            return empty();
        }
        List<List<?>> schema = cast(result.get(1));
        if (isEmpty(schema)) {
            return empty();
        }
        return of(asEntity(readTuple(tuple, fromTuple(schema))));
    }

    public Optional<Entity> delete(String spaceName, long id) {
        return delete(spaceName, setOf(id));
    }

    public List<Entity> deleteAll(String spaceName) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<List<?>> result = cast(callTarantoolFunction(client, DELETE_ALL + spaceName + VALUES_POSTFIX));
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


    public Entity update(String spaceName, Set<?> keys, TarantoolUpdateFieldOperation... operations) {
        List<TarantoolUpdateFieldOperation> operationsWithSchema = stream(operations)
                .filter(operation -> !isEmpty(operation.getSchemaOperation()))
                .collect(toList());
        List<TarantoolUpdateFieldOperation> operationsWithoutSchema = stream(operations)
                .filter(operation -> isEmpty(operation.getSchemaOperation()))
                .collect(toList());
        Entity entity = null;
        if (!isEmpty(operationsWithSchema)) {
            entity = updateWithSchema(spaceName, keys, operationsWithSchema);
        }
        if (!isEmpty(operationsWithoutSchema)) {
            entity = updateWithoutSchema(spaceName, keys, operationsWithoutSchema);
        }
        return entity;
    }

    private Entity updateWithSchema(String spaceName, Set<?> keys, List<TarantoolUpdateFieldOperation> operations) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        String functionName = UPDATE + spaceName + VALUE_POSTFIX + WITH_SCHEMA_POSTFIX;
        List<?> valueOperations = operations
                .stream()
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(toList());
        List<?> schemaOperations = operations
                .stream()
                .map(TarantoolUpdateFieldOperation::getSchemaOperation)
                .collect(toList());
        List<List<?>> result = cast(callTarantoolFunction(client, functionName, fixedArrayOf(fixedArrayOf(keys), valueOperations, schemaOperations)));
        return asEntity(readTuple(result.get(0), fromTuple(result.get(1))));
    }

    private Entity updateWithoutSchema(String spaceName, Set<?> keys, List<TarantoolUpdateFieldOperation> operations) {
        evaluateValueScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        String functionName = UPDATE + spaceName + VALUE_POSTFIX;
        List<List<?>> result = cast(callTarantoolFunction(client, functionName, fixedArrayOf(fixedArrayOf(keys), operations
                .stream()
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(toList()))));
        return asEntity(readTuple(result.get(0), fromTuple(result.get(1))));
    }


    public void upsert(String spaceName, Entity defaultEntity, TarantoolUpdateFieldOperation... operations) {
        if (isNull(defaultEntity)) {
            return;
        }
        if (!defaultEntity.getFieldNames().contains(ID)) {
            throw new TarantoolDaoException(format(ENTITY_WITHOUT_ID_FILED, spaceName));
        }
        if (idCalculationMode == SEQUENCE) {
            defaultEntity.getFields().put(ID, cast(longPrimitive(null)));
        }
        evaluateValueScript(instanceId, spaceName);
        PlainTupleWriterResult valueTupleResult = writeTuple(defaultEntity);
        if (isNull(valueTupleResult)) {
            return;
        }
        List<?> schemaOperations = stream(operations)
                .filter(operation -> !isEmpty(operation.getSchemaOperation()))
                .map(TarantoolUpdateFieldOperation::getSchemaOperation)
                .collect(toList());
        if (isEmpty(schemaOperations)) {
            return;
        }
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        String functionName = UPSERT + spaceName + VALUE_POSTFIX + WITH_SCHEMA_POSTFIX;
        List<?> schemaTuple = valueTupleResult.getSchema().toTuple();
        List<?> valueTuple = valueTupleResult.getTuple();
        List<?> valueOperations = stream(operations)
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(toList());
        callTarantoolFunction(client, functionName, fixedArrayOf(valueTuple, schemaTuple, valueOperations, schemaOperations));
    }
}
