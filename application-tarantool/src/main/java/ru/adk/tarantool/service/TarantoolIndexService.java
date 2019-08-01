package ru.adk.tarantool.service;

import lombok.NoArgsConstructor;
import org.tarantool.TarantoolClient;
import ru.adk.tarantool.configuration.lua.TarantoolIndexConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.tarantool.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.adk.tarantool.configuration.lua.TarantoolIndexConfiguration.*;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Functions.DROP;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.Functions.RENAME;
import static ru.adk.tarantool.executor.TarantoolLuaExecutor.evaluateLuaScript;
import static ru.adk.tarantool.module.TarantoolModule.tarantoolModuleState;

@NoArgsConstructor(access = PRIVATE)
public final class TarantoolIndexService {
    public static void createIndex(String instanceId, String spaceName, String indexName) {
        evaluateLuaScript(instanceId, tarantoolIndex(indexName, spaceName).toCreateIndexLua());
        evaluateLuaScript(instanceId, tarantoolIndex(indexName, spaceName).toManageIndexLua());
    }

    public static void createIndex(String instanceId, TarantoolIndexConfiguration configuration) {
        evaluateLuaScript(instanceId, configuration.toCreateIndexLua());
        evaluateLuaScript(instanceId, configuration.toManageIndexLua());
    }

    public static void dropIndex(String instanceId, String spaceName, String indexName) {
        evaluateLuaScript(instanceId, tarantoolIndex(indexName, spaceName).toManageIndexLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, DROP + spaceName + indexName);
    }

    public static void alterIndex(String instanceId, TarantoolIndexConfiguration newConfiguration) {
        evaluateLuaScript(instanceId, newConfiguration.toAlterIndexLua());
        evaluateLuaScript(instanceId, newConfiguration.toManageIndexLua());
    }

    public static void renameIndex(String instanceId, String spaceName, String currentIndexName, String newIndexName) {
        evaluateLuaScript(instanceId, tarantoolIndex(currentIndexName, spaceName).toManageIndexLua());
        TarantoolClient client = tarantoolModuleState().getClient(instanceId);
        callTarantoolFunction(client, RENAME + spaceName + currentIndexName, fixedArrayOf(newIndexName));
    }
}