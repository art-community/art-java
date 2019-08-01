package ru.art.tarantool.service;

import lombok.NoArgsConstructor;
import org.tarantool.TarantoolClient;
import ru.art.tarantool.configuration.lua.TarantoolSequenceConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.tarantool.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.art.tarantool.configuration.lua.TarantoolSequenceConfiguration.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static ru.art.tarantool.executor.TarantoolLuaExecutor.evaluateLuaScript;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModuleState;
import java.util.List;

@NoArgsConstructor(access = PRIVATE)
public final class TarantoolSequenceService {
    public static void createSequence(String instanceId, String sequenceName) {
        evaluateLuaScript(instanceId, tarantoolSequence(sequenceName).toCreateSequenceLua());
        evaluateLuaScript(instanceId, tarantoolSequence(sequenceName).toManageSequenceLua());
    }

    public static void createSequence(String instanceId, TarantoolSequenceConfiguration sequenceConfiguration) {
        evaluateLuaScript(instanceId, sequenceConfiguration.toCreateSequenceLua());
        evaluateLuaScript(instanceId, sequenceConfiguration.toManageSequenceLua());
    }

    public static void dropSequence(String instanceId, String sequenceName) {
        evaluateLuaScript(instanceId, tarantoolSequence(sequenceName).toManageSequenceLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, DROP + sequenceName);
    }

    public static Integer sequenceNext(String instanceId, String sequenceName) {
        evaluateLuaScript(instanceId, tarantoolSequence(sequenceName).toManageSequenceLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        List<?> result = (List<?>) callTarantoolFunction(client, NEXT + sequenceName).get(0);
        if (isEmpty(result)) return null;
        return (Integer) result.get(0);
    }

    public static void resetSequence(String instanceId, String sequenceName) {
        evaluateLuaScript(instanceId, tarantoolSequence(sequenceName).toManageSequenceLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, RESET + sequenceName);
    }

    public static void setSequence(String instanceId, String sequenceName, int value) {
        evaluateLuaScript(instanceId, tarantoolSequence(sequenceName).toManageSequenceLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, SET + sequenceName, fixedArrayOf(value));
    }
}
