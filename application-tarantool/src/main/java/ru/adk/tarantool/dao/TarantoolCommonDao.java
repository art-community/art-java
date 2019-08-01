package ru.adk.tarantool.dao;

import org.tarantool.TarantoolClient;
import static java.util.Collections.emptySet;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.factory.CollectionsFactory.setOf;
import static ru.adk.tarantool.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolIdCalculationMode;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolIdCalculationMode.MANUAL;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TarantoolIdCalculationMode.SEQUENCE;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModuleState;
import static ru.adk.tarantool.service.TarantoolScriptService.evaluateCommonScript;
import java.util.List;
import java.util.Set;

public class TarantoolCommonDao {
    final String instanceId;
    TarantoolIdCalculationMode idCalculationMode = MANUAL;

    TarantoolCommonDao(String instanceId) {
        this.instanceId = instanceId;
    }

    public long count(String spaceName, Set<?> keys) {
        evaluateCommonScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = callTarantoolFunction(client, COUNT + spaceName, keys);
        if (isEmpty(result)) {
            return 0L;
        }
        return ((Number) ((List<?>) result.get(0)).get(0)).longValue();
    }

    public long count(String spaceName, long id) {
        return count(spaceName, setOf(id));
    }

    public long count(String spaceName) {
        return count(spaceName, emptySet());
    }


    public long len(String spaceName) {
        evaluateCommonScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = callTarantoolFunction(client, LEN + spaceName);
        if (isEmpty(result)) {
            return 0L;
        }
        return ((Number) ((List<?>) result.get(0)).get(0)).longValue();
    }


    public void truncate(String spaceName) {
        evaluateCommonScript(instanceId, spaceName);
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, TRUNCATE + spaceName);
    }

    public void sequencedId() {
        idCalculationMode = SEQUENCE;
    }

    public void manualId() {
        idCalculationMode = MANUAL;
    }
}
